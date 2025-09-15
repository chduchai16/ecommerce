package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> , JpaSpecificationExecutor<Product> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.averageRating = (SELECT COALESCE(AVG(r.rate), 0) FROM Rating r WHERE r.product.id = :productId) WHERE p.id = :productId")
    void updateAverageRating(@Param("productId") int productId);
}
