package com.example.shopapp.repositories;

import com.example.shopapp.models.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> , JpaSpecificationExecutor<CartItem> {

}
