package com.mongodb.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mongodb.document.OfficeDocument;
import com.mongodb.mysql.entity.Employee;
import com.mongodb.mysql.entity.Office;

@Mapper(componentModel = "spring")
public interface OfficeMapper {
	OfficeMapper INSTANCE = Mappers.getMapper(OfficeMapper.class);

	OfficeDocument officeToDocument(Office source);

	default List<Long> map(Set<Employee> value) {
		return value.stream().map(v -> v.getEmployeeNumber()).collect(Collectors.toList());
	}
}
