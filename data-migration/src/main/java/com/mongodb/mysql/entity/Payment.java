package com.mongodb.mysql.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*@Id
	@ManyToOne
	@JoinColumn(name = "customerNumber")
	private Customer customer;*/
	@Id
	@Column(name = "checkNumber")
	private String checkNumber;
	@Column(name = "paymentDate")
	private Date paymentDate;
	@Column(name = "amount")
	private double amount;
}
