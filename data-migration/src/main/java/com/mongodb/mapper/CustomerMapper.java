package com.mongodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mongodb.document.CustomerDocument;
import com.mongodb.mysql.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
	CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

	CustomerDocument customerToDocument(Customer source);
}
