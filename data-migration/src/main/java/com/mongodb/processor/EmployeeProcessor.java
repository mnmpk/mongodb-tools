package com.mongodb.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.mongodb.document.EmployeeDocument;
import com.mongodb.mapper.EmployeeMapper;
import com.mongodb.mysql.entity.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee, EmployeeDocument> {

	private static final Logger log = LoggerFactory.getLogger(EmployeeProcessor.class);

	@Override
	public EmployeeDocument process(final Employee employee) throws Exception {
		EmployeeDocument transformedEmployee = EmployeeMapper.INSTANCE.employeeToDocument(employee);
		log.info("Converting (" + employee + ") into (" + transformedEmployee + ")");

		return transformedEmployee;
	}
}
