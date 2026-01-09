package com.example.shopapp.services;


import com.example.shopapp.models.entities.ProductImage;

public interface IProductImageService {
    ProductImage createProductImage(ProductImage productImage) throws Exception;
    void deleteProductImage(int id) throws Exception;
}
