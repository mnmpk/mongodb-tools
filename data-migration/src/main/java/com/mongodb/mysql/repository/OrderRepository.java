package com.mongodb.mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mongodb.mysql.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	public Page<Order> findByStatusGreaterThanAndStatusLessThanEqualOrderByStatusAsc(String min, String max,
			Pageable pageable);

	public Page<Order> findByStatusGreaterThanOrderByStatusAsc(String min, Pageable pageable);
}
