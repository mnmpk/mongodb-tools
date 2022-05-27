package com.mongodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mongodb.document.ProductLineDocument;
import com.mongodb.mysql.entity.ProductLine;

@Mapper(componentModel = "spring")
public interface ProductLineMapper {
	ProductLineMapper INSTANCE = Mappers.getMapper(ProductLineMapper.class);

	@Mapping(target = "image", ignore = true)
	ProductLineDocument productLineToDocument(ProductLine source);
}
