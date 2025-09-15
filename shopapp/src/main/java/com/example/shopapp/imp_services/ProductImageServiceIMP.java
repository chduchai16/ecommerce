package com.example.shopapp.imp_services;

import com.example.domain.models.entities.ProductImage;
import com.example.domain.persistence.repositories.ProductImageRepository;
import com.example.domain.services.IProductImageService;
import jakarta.persistence.EntityNotFoundException;
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
            throw new EntityNotFoundException("Ảnh sản phẩm này không tồn tại");
        }
        else {
            productImageRepository.deleteById(productImageId);
        }
    }
}
