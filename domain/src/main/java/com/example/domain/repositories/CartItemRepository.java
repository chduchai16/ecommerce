package com.example.domain.repositories;

import com.example.domain.entities.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    void findByCartId(int cartId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    void deleteCartItemsByCartId(int cartId );
}
