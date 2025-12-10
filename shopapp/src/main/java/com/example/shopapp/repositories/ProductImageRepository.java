package com.example.shopapp.repositories;

import com.example.shopapp.models.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> , JpaSpecificationExecutor<ProductImage> {
}
