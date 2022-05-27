package com.mongodb.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mongodb.mysql.entity.Office;

public interface OfficeRepository extends JpaRepository<Office, String> {
}
