package com.mongodb.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.mongodb.document.CustomerDocument;
import com.mongodb.document.EmployeeDocument;
import com.mongodb.document.OfficeDocument;
import com.mongodb.document.OrderDocument;
import com.mongodb.document.ProductLineDocument;
import com.mongodb.listener.JobCompletionNotificationListener;
import com.mongodb.listener.StepFinishListener;
import com.mongodb.mysql.entity.Customer;
import com.mongodb.mysql.entity.Employee;
import com.mongodb.mysql.entity.Office;
import com.mongodb.mysql.entity.Order;
import com.mongodb.mysql.entity.ProductLine;
import com.mongodb.mysql.repository.CustomerRepository;
import com.mongodb.mysql.repository.EmployeeRepository;
import com.mongodb.mysql.repository.OfficeRepository;
import com.mongodb.mysql.repository.OrderRepository;
import com.mongodb.mysql.repository.ProductLineRepository;
import com.mongodb.processor.CustomerProcessor;
import com.mongodb.processor.EmployeeProcessor;
import com.mongodb.processor.OfficeProcessor;
import com.mongodb.processor.OrderProcessor;
import com.mongodb.processor.ProductLineProcessor;
import com.mongodb.repository.CustomerDocumentRepository;
import com.mongodb.repository.EmployeeDocumentRepository;
import com.mongodb.repository.OfficeDocumentRepository;
import com.mongodb.repository.OrderDocumentRepository;
import com.mongodb.repository.ProductLineDocumentRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

	@Value(value = "${batch.querySize:100}")
	private int querySize;
	@Value(value = "${batch.pageSize:100}")
	private int pageSize;
	@Value(value = "${batch.chunkSize:100}")
	private int chunkSize;
	@Value(value = "${batch.poolSize:100}")
	private int poolSize;
	@Value(value = "${batch.start")
	private String start;
	@Value(value = "${batch.end}")
	private String end;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public CustomerRepository customerRepository;

	@Autowired
	public CustomerDocumentRepository customerDocumentRepository;

	@Autowired
	public OrderRepository orderRepository;

	@Autowired
	public OrderDocumentRepository orderDocumentRepository;

	@Autowired
	public OfficeRepository officeRepository;

	@Autowired
	public OfficeDocumentRepository officeDocumentRepository;

	@Autowired
	public EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeDocumentRepository employeeDocumentRepository;

	@Autowired
	public ProductLineRepository productLineRepository;

	@Autowired
	public ProductLineDocumentRepository productLineDocumentRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Bean
	public TaskExecutor taskExecutor() {
		// SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new
		// SimpleAsyncTaskExecutor("spring_batch");
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(poolSize);
		return threadPoolTaskExecutor;
	}

	@Bean
	public Job dataMigrationJob(JobCompletionNotificationListener jobCompletionNotificationListener,
			StepFinishListener stepFinishListener, DataSource sourceDataSource) {
		SimpleJobBuilder jobBuilder = jobBuilderFactory.get("dataMigrationJob").incrementer(new RunIdIncrementer())
				.listener(jobCompletionNotificationListener)
				.start(stepBuilderFactory.get("firstStep").tasklet((a, b) -> {
					return RepeatStatus.FINISHED;
				}).build());
		String query = "select id from(" + "select orderNumber as id, (@row_number:=@row_number + 1) AS RowNum "
				+ "from orders where status >= '" + start + "' and status < '" + end
				+ "' order by status ASC) a where a.RowNum%" + querySize + "=0";
		log.info(query);
		try (Statement stmt = sourceDataSource.getConnection().createStatement()) {
			stmt.execute("SET @row_number = 0;");
			ResultSet rs = stmt.executeQuery(query);
			long lastId = 0l;
			while (rs.next()) {
				Long id = rs.getLong("id");
				jobBuilder.next(migrateOrderStep(lastId, id, stepFinishListener));
				lastId = id;
			}
			jobBuilder.next(migrateOrderStep(lastId, null, stepFinishListener));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jobBuilder.next(migrateCustomerStep()).next(migrateOfficeStep()).next(migrateEmployeeStep())
				.next(migrateProductLineStep());
		return jobBuilder.build();
	}

	@Bean
	public Step migrateCustomerStep() {
		return stepBuilderFactory.get("migrateCustomerStep").<Customer, CustomerDocument>chunk(10)
				.reader(customerReader()).processor(customerProcessor()).writer(customerWriter()).build();
	}

	@Bean
	public Step migrateOrderStep(Long min, Long max, StepFinishListener stepFinishListener) {
		return stepBuilderFactory.get("migrateOrderStep").listener(stepFinishListener)
				.<Order, OrderDocument>chunk(chunkSize).reader(orderReader(min, max)).processor(orderProcessor())
				.writer(orderWriter()).taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Step migrateOfficeStep() {
		return stepBuilderFactory.get("migrateOfficeStep").<Office, OfficeDocument>chunk(chunkSize)
				.reader(officeReader()).processor(officeProcessor()).writer(officeWriter()).taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	public Step migrateEmployeeStep() {
		return stepBuilderFactory.get("migrateEmployeeStep").<Employee, EmployeeDocument>chunk(chunkSize)
				.reader(employeeReader()).processor(employeeProcessor()).writer(employeeWriter())
				.taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Step migrateProductLineStep() {
		return stepBuilderFactory.get("migrateProductLineStep").<ProductLine, ProductLineDocument>chunk(chunkSize)
				.reader(productLineReader()).processor(productLineProcessor()).writer(productLineWriter())
				.taskExecutor(taskExecutor()).build();
	}

	@Bean
	public CustomerProcessor customerProcessor() {
		return new CustomerProcessor();
	}

	@Bean
	public ItemReader<Customer> customerReader() {
		return new RepositoryItemReaderBuilder<Customer>().repository(customerRepository).name("customerReader")
				.methodName("findAll").pageSize(pageSize).sorts(Collections.singletonMap("customerNumber", Sort.Direction.ASC)).build();
	}

	@Bean
	public MongoItemWriter<CustomerDocument> customerWriter() {
		return new MongoItemWriterBuilder<CustomerDocument>().template(mongoTemplate).collection("customers").build();
	}

	@Bean
	public OrderProcessor orderProcessor() {
		return new OrderProcessor();
	}

	@Bean
	public ItemReader<Order> orderReader(Long min, Long max) {
		if (max == null) {
			return new RepositoryItemReaderBuilder<Order>().repository(orderRepository).name("orderReader")
					.methodName("findByStatusGreaterThanOrderByStatusAsc").arguments(min).pageSize(pageSize)
					.sorts(Collections.singletonMap("status", Sort.Direction.ASC)).build();
		}
		return new RepositoryItemReaderBuilder<Order>().repository(orderRepository).name("orderReader")
				.methodName("findByStatusGreaterThanAndStatusLessThanEqualOrderByStatusAsc").arguments(min, max)
				.pageSize(pageSize).sorts(Collections.singletonMap("status", Sort.Direction.ASC)).build();
	}

	@Bean
	public MongoItemWriter<OrderDocument> orderWriter() {
		return new MongoItemWriterBuilder<OrderDocument>().template(mongoTemplate).collection("orders").build();
	}

	@Bean
	public OfficeProcessor officeProcessor() {
		return new OfficeProcessor();
	}

	@Bean
	public ItemReader<Office> officeReader() {
		return new RepositoryItemReaderBuilder<Office>().repository(officeRepository).name("officeReader")
				.methodName("findAll").pageSize(pageSize).sorts(Collections.singletonMap("officeCode", Sort.Direction.ASC)).build();
	}

	@Bean
	public MongoItemWriter<OfficeDocument> officeWriter() {
		return new MongoItemWriterBuilder<OfficeDocument>().template(mongoTemplate).collection("offices").build();
	}

	@Bean
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}

	@Bean
	public ItemReader<Employee> employeeReader() {
		return new RepositoryItemReaderBuilder<Employee>().repository(employeeRepository).name("officeReader")
				.methodName("findAll").pageSize(pageSize).sorts(Collections.singletonMap("employeeNumber", Sort.Direction.ASC)).build();
	}

	@Bean
	public MongoItemWriter<EmployeeDocument> employeeWriter() {
		return new MongoItemWriterBuilder<EmployeeDocument>().template(mongoTemplate).collection("employees").build();
	}

	@Bean
	public ProductLineProcessor productLineProcessor() {
		return new ProductLineProcessor();
	}

	@Bean
	public ItemReader<ProductLine> productLineReader() {
		return new RepositoryItemReaderBuilder<ProductLine>().repository(productLineRepository).name("officeReader")
				.methodName("findAll").pageSize(pageSize).sorts(Collections.singletonMap("productLine", Sort.Direction.ASC)).build();
	}

	@Bean
	public MongoItemWriter<ProductLineDocument> productLineWriter() {
		return new MongoItemWriterBuilder<ProductLineDocument>().template(mongoTemplate).collection("productlines")
				.build();
	}
}
