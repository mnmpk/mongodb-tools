package com.mongodb.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.mongodb.mapper.CustomerMapper;
import com.mongodb.mysql.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer, com.mongodb.document.CustomerDocument> {

	private static final Logger log = LoggerFactory.getLogger(CustomerProcessor.class);

	@Override
	public com.mongodb.document.CustomerDocument process(final Customer customer) throws Exception {
		com.mongodb.document.CustomerDocument transformedCustomer = CustomerMapper.INSTANCE
				.customerToDocument(customer);
		log.info("Converting (" + customer + ") into (" + transformedCustomer + ")");

		return transformedCustomer;
	}
}
