package com.mongodb.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.mongodb.document.OrderDocument;
import com.mongodb.mapper.OrderMapper;
import com.mongodb.mysql.entity.Order;

public class OrderProcessor implements ItemProcessor<Order, OrderDocument> {

	private static final Logger log = LoggerFactory.getLogger(OrderProcessor.class);

	@Override
	public OrderDocument process(final Order order) throws Exception {
		OrderDocument transformedOrder = OrderMapper.INSTANCE.orderToDocument(order);
		log.info("Converting (" + order + ") into (" + transformedOrder + ")");

		return transformedOrder;
	}
}
