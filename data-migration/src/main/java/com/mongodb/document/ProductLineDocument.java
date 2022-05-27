package com.mongodb.document;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "productLine")
public class ProductLineDocument implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String productLine;
	private String textDescription;
	private String htmlDescription;
	private String image;
	private List<ProductDocument> products;
}
