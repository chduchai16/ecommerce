package com.example.shopapp.services;

import com.example.shopapp.models.entities.ProductImage;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.specifications.ProductImageSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageServiceIMP implements IProductImageService {

    private final ProductImageRepository productImageRepository ;

    @Override
    public ProductImage createProductImage(ProductImage productImage) throws Exception {
        Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasImageName(productImage.getImageName()));
        Optional<ProductImage> existingImage = productImageRepository.findOne(spec);
        if (existingImage.isPresent()) {
            throw new Exception("Ảnh sản phẩm đã tồn tại");
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public void deleteProductImage(int productImageId) throws Exception {
        Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasId(productImageId));
        Optional<ProductImage> existingImage = productImageRepository.findOne(spec);
        if (existingImage.isEmpty()) {
            throw new EntityNotFoundException("Ảnh sản phẩm này không tồn tại");
        }
        productImageRepository.deleteById(productImageId);
    }
}
