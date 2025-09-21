package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.*;
import com.example.domain.persistence.repositories.CartItemRepository;
import com.example.domain.persistence.repositories.CategoryRepository;
import com.example.domain.persistence.repositories.ProductImageRepository;
import com.example.domain.persistence.repositories.SellerRepository;
import com.example.shopapp.transfer.dtos.requests.ProductDTO;
import com.example.shopapp.transfer.dtos.responses.ProductImageResponse;
import com.example.shopapp.transfer.dtos.responses.ProductResponse;
import com.example.shopapp.transfer.dtos.responses.ProductSpecificationResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CartItemRepository cartItemRepository;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ProductImageMapper productImageMapper;
    private final SellerMapper sellerMapper;
    private final ProductSpecificationMapper productSpecificationMapper;

    private TypeMap<ProductDTO, Product> fromRequestToEntityTypeMap;
    private TypeMap<Product, ProductResponse> fromEntityToResponseTypeMap;

    public Product fromRequestToEntity(ProductDTO productDTO) {
        if (productDTO == null)
            return null;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = this.modelMapper.createTypeMap(ProductDTO.class, Product.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(Product::setCategory);
                mapper.skip(Product::setCartItems);
                mapper.skip(Product::setProductImages);
                mapper.skip(Product::setSeller);
                mapper.skip(Product::setSpecifications);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        Product product = fromRequestToEntityTypeMap.map(productDTO);

        // map category
        Category category = this.categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Danh mục với id " + productDTO.getCategoryId() + " không tồn tại"));
        product.setCategory(category);

        // map seller
        if (productDTO.getSellerId() != null) {
            Seller seller = this.sellerRepository.findById(productDTO.getSellerId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Người bán với id " + productDTO.getSellerId() + " không tồn tại"));
            product.setSeller(seller);
        }

        // map Product Image
        if (productDTO.getProductImageIds() != null && !productDTO.getProductImageIds().isEmpty()) {
            List<ProductImage> productImages = this.productImageRepository.findAllById(productDTO.getProductImageIds());
            product.setProductImages(productImages);
        }

        // map Cart items
        if (productDTO.getCartItemIds() != null && !productDTO.getCartItemIds().isEmpty()) {
            List<CartItem> cartItems = this.cartItemRepository.findAllById(productDTO.getCartItemIds());
            product.setCartItems(cartItems);
        }
        return product;
    }

    public ProductResponse fromEntityToResponse(Product product) {
        if (product == null)
            return null;
        if (fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Product.class, ProductResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(ProductResponse::setCategoryName);
                mapper.skip(ProductResponse::setProductImageResponses);
                mapper.skip(ProductResponse::setSeller);
                mapper.skip(ProductResponse::setSpecifications);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }

        ProductResponse productResponse = fromEntityToResponseTypeMap.map(product);

        // map category
        if (product.getCategory() != null) {
            productResponse.setCategoryName(product.getCategory().getName());
        }

        // map images
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            List<ProductImageResponse> productImageResponses = product.getProductImages()
                    .stream()
                    .map(productImageMapper::fromEntityToResponse)
                    .toList();
            productResponse.setProductImageResponses(productImageResponses);
        }

        // map seller
        if (product.getSeller() != null) {
            productResponse.setSeller(sellerMapper.fromEntityToResponse(product.getSeller()));
        }

        // map specifications
        if (product.getSpecifications() != null && !product.getSpecifications().isEmpty()) {
            List<ProductSpecificationResponse> specificationResponses = product.getSpecifications()
                    .stream()
                    .map(productSpecificationMapper::fromEntityToResponse)
                    .toList();
            productResponse.setSpecifications(specificationResponses);
        }

        return productResponse;
    }
}
