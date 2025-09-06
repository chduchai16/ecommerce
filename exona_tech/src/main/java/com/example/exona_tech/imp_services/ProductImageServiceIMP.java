package com.example.exona_tech.imp_services;

import com.example.domain.entities.ProductImage;
import com.example.domain.repositories.ProductImageRepository;
import com.example.domain.services.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageServiceIMP implements IProductImageService {

    private final ProductImageRepository productImageRepository ;

    @Override
    public ProductImage createProductImage(ProductImage productImage) throws Exception {
        return this.productImageRepository.save(productImage) ;
    }

    @Override
    public void deleteProductImage(int productImageId) throws Exception {
        if (productImageRepository.findById(productImageId).isEmpty()){
            throw new Exception("This product image does not exist");
        }
        else {
            productImageRepository.deleteById(productImageId);
        }
    }
}
