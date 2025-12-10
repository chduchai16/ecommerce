package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> , JpaSpecificationExecutor<OrderDetail> {
}

