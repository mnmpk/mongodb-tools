package com.mongodb.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mongodb.mysql.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
