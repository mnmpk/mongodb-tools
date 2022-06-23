package com.mongodb.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.document.OrderDocument;
import com.mongodb.mysql.entity.Order;
import com.mongodb.mysql.repository.CustomerRepository;
import com.mongodb.mysql.repository.OrderRepository;
import com.mongodb.repository.CustomerDocumentRepository;
import com.mongodb.repository.OrderDocumentRepository;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	@Autowired
	private CustomerDocumentRepository customerDocumentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderDocumentRepository orderDocumentRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Value(value = "${batch.validate.ids}")
	private long[] ids;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
			long mongoDBCount = customerDocumentRepository.count();
			long mySqlCount = customerRepository.count();
			log.info("Found " + mongoDBCount + " customers in the MongoDB. matched with the MySQL no. of customers:"
					+ (mongoDBCount == mySqlCount));

			log.info("!!! Start verifying sample data");
			for (long id : ids) {

				OrderDocument mongodb = orderDocumentRepository.findById(id).get();
				Order mysql = orderRepository.findById(id).get();
				if (mongodb != null && mysql != null) {
					log.info("Verifying id:{}=={}?{}", mongodb.getOrderNumber(), mysql.getOrderNumber(),
							mongodb.getOrderNumber().equals(mysql.getOrderNumber()));
					log.info("Verifying orderDate:{}=={}?{}", mongodb.getOrderDate(), mysql.getOrderDate().getTime(),
							mongodb.getOrderDate().getTime() == mysql.getOrderDate().getTime());

				} else {
					log.error("record not found:" + id);
				}
			}
		}
	}
}
