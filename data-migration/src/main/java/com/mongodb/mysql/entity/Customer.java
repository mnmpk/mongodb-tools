package com.mongodb.mysql.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "customers")
public class Customer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "customerNumber")
	private long customerNumber;
	@Column(name = "customerName")
	private String customerName;
	@Column(name = "contactLastName")
	private String contactLastName;
	@Column(name = "contactFirstName")
	private String contactFirstName;
	@Column(name = "phone")
	private String phone;
	@Column(name = "addressLine1")
	private String addressLine1;
	@Column(name = "addressLine2")
	private String addressLine2;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "postalCode")
	private String postalCode;
	@Column(name = "country")
	private String country;
	@ManyToOne()
	@JoinColumn(name = "salesRepEmployeeNumber")
	private Employee salesRepEmployee;
	private double creditLimit;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "customerNumber")
	@ToString.Exclude
	private Set<Payment> payments;
}
