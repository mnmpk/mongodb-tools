package com.mongodb.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mongodb.mysql.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
