package com.mongodb.mysql.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "productlines")
public class ProductLine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String productLine;
	private String textDescription;
	private String htmlDescription;
	private Blob image;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "productLine")
	@ToString.Exclude
	private Set<Product> products;
}
