package com.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.document.CustomerDocument;

public interface CustomerDocumentRepository extends MongoRepository<CustomerDocument, Long> {
}