package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.ProductDTO;
import com.example.domain.dtos.resposnes.CategoryResponse;
import com.example.domain.dtos.resposnes.ProductImageResponse;
import com.example.domain.dtos.resposnes.ProductResponse;
import com.example.domain.entities.*;
import com.example.domain.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final SupplierRepository supplierRepository ;
    private final CategoryRepository categoryRepository ;
    private final ProductImageRepository productImageRepository ;
    private final CartItemRepository cartItemRepository ;
    private final ModelMapper modelMapper ;
    private final CategoryMapper categoryMapper ;
    private final ProductImageMapper productImageMapper ;

    private TypeMap<ProductDTO , Product> fromRequestToEntityTypeMap;
    private TypeMap<Product , ProductResponse> fromEntityToResponseTypeMap ;

    public Product fromRequestToEntityTypeMap( ProductDTO productDTO ){
        if(productDTO == null) return null ;
        if(fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = this.modelMapper.createTypeMap(ProductDTO.class , Product.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(Product :: setCategory);
                mapper.skip(Product :: setCartItems);
                mapper.skip(Product :: setProductImages);
                mapper.skip(Product :: setSupplier);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        Product product = fromRequestToEntityTypeMap.map(productDTO) ;

        // map category
        Category category = this.categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(()-> new EntityNotFoundException("This category does not exist"));
        product.setCategory(category);
        // map supplier
        if(productDTO.getSupplierId() != null){
            Supplier supplier = this.supplierRepository.findById(productDTO.getSupplierId()).orElseThrow(()-> new EntityNotFoundException("This supplier does not exist"));
            product.setSupplier(supplier);
        }
        // map Product Image
        if(productDTO.getProductImageIds() != null && !productDTO.getProductImageIds().isEmpty()) {
            List<ProductImage> productImages = this.productImageRepository.findAllById(productDTO.getProductImageIds());
            product.setProductImages(productImages);
        }
        // map Cart items
        if( productDTO.getCartItemIds() != null && !productDTO.getCartItemIds().isEmpty()){
            List<CartItem> cartItems = this.cartItemRepository.findAllById(productDTO.getCartItemIds());
            product.setCartItems(cartItems);
        }
        return product ;
    }

    public ProductResponse fromEntityToResponse (Product product) {
        if (product== null) return null ;
        if (fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Product.class , ProductResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(ProductResponse :: setCategoryResponse);
                mapper.skip(ProductResponse :: setProductImageResponses);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }

        ProductResponse productResponse = fromEntityToResponseTypeMap.map(product);

        // map category
        if (product.getCategory() != null) {
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(product.getCategory());
            productResponse.setCategoryResponse(categoryResponse);
        }

        // map images
        if(product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            List<ProductImageResponse> productImageResponses = product.getProductImages()
                    .stream()
                    .map(entity -> productImageMapper.fromEntityToResponse(entity))
                    .toList();
            productResponse.setProductImageResponses(productImageResponses);
        }
        return productResponse;
    }
}
