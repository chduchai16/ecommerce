package com.example.domain.services;

import com.example.domain.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product getProductById(int productId) throws Exception;
    Product createProduct(Product product) throws Exception;
    Product updateProduct(Product product) throws Exception;
    void deleteProduct(int productId) throws Exception;
    void viewProduct (int productId) throws Exception;
    Page<Product> getHotProducts(Pageable pageable);
    List<Product> createProducts(List<Product> products) throws Exception;

    // cải tiến
    Page<Product> filterProducts(
            String name,
            String categoryName,
            String color,
            String brand,
            Float minPrice,
            Float maxPrice,
            Integer minStock,
            Float minRating,
            String description,
            Long minViews,
            Pageable pageable
    );
}
