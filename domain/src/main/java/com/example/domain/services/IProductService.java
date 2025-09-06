package com.example.domain.services;

import com.example.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product getProductById(int productId) throws Exception;
    Page<Product> getAllProducts(Pageable pageable);
    Product createProduct(Product product) throws Exception;
    Product updateProduct(Product product) throws Exception;
    void deleteProduct(int productId) throws Exception;
    Page<Product> searchProducts(String keyword, Pageable pageable) ;
    Page<Product> getProductsByCategory(int categoryId, Pageable pageable) ;
    void viewProduct (int productId) throws Exception;
    List<Product> getHotProducts(int limit);
    List<Product> getTop10BestSellingProducts();
    List<Product> createProducts(List<Product> products) throws Exception;
}
