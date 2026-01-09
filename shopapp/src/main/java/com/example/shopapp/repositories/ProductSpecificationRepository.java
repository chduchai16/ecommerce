package com.example.shopapp.repositories;

import com.example.shopapp.models.entities.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecificationRepository
        extends JpaRepository<ProductSpecification, Integer>, JpaSpecificationExecutor<ProductSpecification> {
}
