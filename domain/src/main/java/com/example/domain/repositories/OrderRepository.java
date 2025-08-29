package com.example.domain.repositories;

import com.example.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAll(Pageable pageable) ;
    Page<Order> findOrdersByUserId(int userId, Pageable pageable);
}
