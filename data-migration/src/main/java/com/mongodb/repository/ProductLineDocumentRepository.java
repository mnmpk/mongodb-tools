package com.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.document.ProductLineDocument;

public interface ProductLineDocumentRepository extends MongoRepository<ProductLineDocument, String> {
}