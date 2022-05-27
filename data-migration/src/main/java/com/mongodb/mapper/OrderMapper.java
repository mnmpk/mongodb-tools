package com.mongodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.mongodb.document.OrderDocument;
import com.mongodb.mysql.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

	@Mappings({ @Mapping(target = "customer.salesRepEmployee", ignore = true),
			@Mapping(target = "customer.payments", ignore = true) })
	OrderDocument orderToDocument(Order source);
}
