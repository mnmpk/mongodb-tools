package com.mongodb.document;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class OrderDetailDocument implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProductDocument product;
	private Integer quantityOrdered;
	private Double priceEach;
	private Long orderLineNumber;
}
