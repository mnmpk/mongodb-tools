package com.mongodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mongodb.document.EmployeeDocument;
import com.mongodb.mysql.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

	EmployeeDocument employeeToDocument(Employee source);
}
