package com.mongodb.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.mysql.repository.CustomerRepository;
import com.mongodb.repository.CustomerDocumentRepository;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	@Autowired
	private CustomerDocumentRepository customerDocumentRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
			long mongoDBCount = customerDocumentRepository.count();
			long mySqlCount = customerRepository.count();
			log.info("Found " + mongoDBCount + " customers in the MongoDB. matched with the MySQL no. of customers:"+(mongoDBCount==mySqlCount));

		}
	}
}
