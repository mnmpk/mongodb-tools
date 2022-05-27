package com.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.document.OfficeDocument;

public interface OfficeDocumentRepository extends MongoRepository<OfficeDocument, String> {
}