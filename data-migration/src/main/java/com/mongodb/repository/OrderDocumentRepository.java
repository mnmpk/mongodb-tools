package com.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.document.OrderDocument;

public interface OrderDocumentRepository extends MongoRepository<OrderDocument, Long> {
}