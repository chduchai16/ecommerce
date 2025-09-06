package com.example.domain.services;


import com.example.domain.entities.ProductImage;

public interface IProductImageService {
    ProductImage createProductImage(ProductImage productImage) throws Exception;
    void deleteProductImage(int id) throws Exception;
}
