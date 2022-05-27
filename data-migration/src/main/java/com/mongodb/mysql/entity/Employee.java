package com.mongodb.mysql.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "employeeNumber")
	private Long employeeNumber;
	@Column(name = "lastName")
	private String lastName;
	@Column(name = "firstName")
	private String firstName;
	@Column(name = "extension")
	private String extension;
	@Column(name = "email")
	private String email;
	@Column(name = "officeCode")
	private String officeCode;
	@Column(name = "reportsTo")
	private Long reportsTo;
	@Column(name = "jobTitle")
	private String jobTitle;
	/*@ToString.Exclude
	@OneToMany(mappedBy = "salesRepEmployee")
	private Set<Customer> customers;*/
}
