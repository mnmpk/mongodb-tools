package com.mongodb.document;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "payment")
public class PaymentDocument implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String checkNumber;
	private Date paymentDate;
	private Double amount;
}
