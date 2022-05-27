package com.mongodb.document;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "product")
public class ProductDocument implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String productCode;
	private String productName;
	private String productScale;
	private String productVendor;
	private String productDescription;
	private Integer quantityInStock;
	private Double buyPrice;
	private Double MSRP;
}
