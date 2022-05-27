package com.mongodb.config;

import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import com.mongodb.document.CustomerDocument;
import com.mongodb.document.EmployeeDocument;
import com.mongodb.document.OfficeDocument;
import com.mongodb.document.OrderDocument;
import com.mongodb.document.ProductLineDocument;
import com.mongodb.listener.JobCompletionNotificationListener;
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

	@Bean
	public Job dataMigrationJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("dataMigrationJob").incrementer(new RunIdIncrementer()).listener(listener)
				.start(migrateCustomerStep())
				.next(migrateOrderStep())
				.next(migrateOfficeStep())
				.next(migrateEmployeeStep())
				.next(migrateProductLineStep())
				.build();
	}

	@Bean
	public Step migrateCustomerStep() {
		return stepBuilderFactory.get("migrateCustomerStep").<Customer, CustomerDocument>chunk(10)
				.reader(customerReader()).processor(customerProcessor()).writer(customerWriter()).build();
	}

	@Bean
	public Step migrateOrderStep() {
		return stepBuilderFactory.get("migrateOrderStep").<Order, OrderDocument>chunk(10).reader(orderReader())
				.processor(orderProcessor()).writer(orderWriter()).build();
	}

	@Bean
	public Step migrateOfficeStep() {
		return stepBuilderFactory.get("migrateOfficeStep").<Office, OfficeDocument>chunk(10).reader(officeReader())
				.processor(officeProcessor()).writer(officeWriter()).build();
	}
	@Bean
	public Step migrateEmployeeStep() {
		return stepBuilderFactory.get("migrateEmployeeStep").<Employee, EmployeeDocument>chunk(10).reader(employeeReader())
				.processor(employeeProcessor()).writer(employeeWriter()).build();
	}

	@Bean
	public Step migrateProductLineStep() {
		return stepBuilderFactory.get("migrateProductLineStep").<ProductLine, ProductLineDocument>chunk(10).reader(productLineReader())
				.processor(productLineProcessor()).writer(productLineWriter()).build();
	}
	

	@Bean
	public CustomerProcessor customerProcessor() {
		return new CustomerProcessor();
	}

	@Bean
	public ItemReader<Customer> customerReader() {
		return new RepositoryItemReaderBuilder<Customer>().repository(customerRepository).name("customerReader")
				.methodName("findAll").sorts(Collections.singletonMap("customerNumber", Sort.Direction.ASC)).build();
	}

	@Bean
	public ItemWriter<CustomerDocument> customerWriter() {
		return new RepositoryItemWriterBuilder<CustomerDocument>().repository(customerDocumentRepository)
				.methodName("insert").build();
	}

	@Bean
	public OrderProcessor orderProcessor() {
		return new OrderProcessor();
	}

	@Bean
	public ItemReader<Order> orderReader() {
		return new RepositoryItemReaderBuilder<Order>().repository(orderRepository).name("orderReader")
				.methodName("findAll").sorts(Collections.singletonMap("orderNumber", Sort.Direction.ASC)).build();
	}

	@Bean
	public ItemWriter<OrderDocument> orderWriter() {
		return new RepositoryItemWriterBuilder<OrderDocument>().repository(orderDocumentRepository).methodName("insert")
				.build();
	}

	@Bean
	public OfficeProcessor officeProcessor() {
		return new OfficeProcessor();
	}

	@Bean
	public ItemReader<Office> officeReader() {
		return new RepositoryItemReaderBuilder<Office>().repository(officeRepository).name("officeReader")
				.methodName("findAll").sorts(Collections.singletonMap("officeCode", Sort.Direction.ASC)).build();
	}

	@Bean
	public ItemWriter<OfficeDocument> officeWriter() {
		return new RepositoryItemWriterBuilder<OfficeDocument>().repository(officeDocumentRepository)
				.methodName("insert").build();
	}

	@Bean
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}

	@Bean
	public ItemReader<Employee> employeeReader() {
		return new RepositoryItemReaderBuilder<Employee>().repository(employeeRepository).name("officeReader")
				.methodName("findAll").sorts(Collections.singletonMap("employeeNumber", Sort.Direction.ASC)).build();
	}

	@Bean
	public ItemWriter<EmployeeDocument> employeeWriter() {
		return new RepositoryItemWriterBuilder<EmployeeDocument>().repository(employeeDocumentRepository)
				.methodName("insert").build();
	}

	@Bean
	public ProductLineProcessor productLineProcessor() {
		return new ProductLineProcessor();
	}

	@Bean
	public ItemReader<ProductLine> productLineReader() {
		return new RepositoryItemReaderBuilder<ProductLine>().repository(productLineRepository).name("officeReader")
				.methodName("findAll").sorts(Collections.singletonMap("productLine", Sort.Direction.ASC)).build();
	}

	@Bean
	public ItemWriter<ProductLineDocument> productLineWriter() {
		return new RepositoryItemWriterBuilder<ProductLineDocument>().repository(productLineDocumentRepository)
				.methodName("insert").build();
	}
}
