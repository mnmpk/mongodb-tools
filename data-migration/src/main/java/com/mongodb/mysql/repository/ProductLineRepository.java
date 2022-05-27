package com.mongodb.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mongodb.mysql.entity.ProductLine;

public interface ProductLineRepository extends JpaRepository<ProductLine, String> {
}
