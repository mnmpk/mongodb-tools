package com.mongodb.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mongodb.mysql.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
