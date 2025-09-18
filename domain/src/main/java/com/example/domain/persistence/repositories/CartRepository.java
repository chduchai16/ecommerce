package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> , JpaSpecificationExecutor<Cart> {
}
