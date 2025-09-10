package com.example.exona_tech.mappers;

import com.example.exona_tech.dtos.requests.ProductDTO;
import com.example.exona_tech.dtos.resposnes.ProductImageResponse;
import com.example.exona_tech.dtos.resposnes.ProductResponse;
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
    private final ProductImageMapper productImageMapper ;

    private TypeMap<ProductDTO , Product> fromRequestToEntityTypeMap;
    private TypeMap<Product , ProductResponse> fromEntityToResponseTypeMap ;

    public Product fromRequestToEntity( ProductDTO productDTO ){
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
        Category category = this.categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Danh mục với id " + productDTO.getCategoryId() + " không tồn tại"));
        product.setCategory(category);

        // map supplier
        if(productDTO.getSupplierId() != null){
            Supplier supplier = this.supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Nhà cung cấp với id " + productDTO.getSupplierId() + " không tồn tại"));
            product.setSupplier(supplier);
        }

        // map Product Image
        if(productDTO.getProductImageIds() != null && !productDTO.getProductImageIds().isEmpty()) {
            List<ProductImage> productImages = this.productImageRepository.findAllById(productDTO.getProductImageIds());
            product.setProductImages(productImages);
        }

        // map Cart items
        if(productDTO.getCartItemIds() != null && !productDTO.getCartItemIds().isEmpty()){
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
                mapper.skip(ProductResponse :: setCategoryName);
                mapper.skip(ProductResponse :: setProductImageResponses);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }

        ProductResponse productResponse = fromEntityToResponseTypeMap.map(product);

        // map category
        if (product.getCategory() != null) {
            productResponse.setCategoryName(product.getCategory().getName());
        }

        // map images
        if(product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            List<ProductImageResponse> productImageResponses = product.getProductImages()
                    .stream()
                    .map(productImageMapper::fromEntityToResponse)
                    .toList();
            productResponse.setProductImageResponses(productImageResponses);
        }
        return productResponse;
    }
}
