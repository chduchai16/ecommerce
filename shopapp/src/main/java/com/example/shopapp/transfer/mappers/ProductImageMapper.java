package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Product;
import com.example.domain.models.entities.ProductImage;
import com.example.domain.persistence.repositories.ProductRepository;
import com.example.shopapp.transfer.dtos.requests.ProductImageDTO;
import com.example.shopapp.transfer.dtos.responses.ProductImageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {
    private final ModelMapper modelMapper ;
    private final ProductRepository productRepository ;

    private TypeMap<ProductImageDTO, ProductImage> fromRequestToEntityTypeMap ;
    private TypeMap<ProductImage , ProductImageResponse> fromEntityToResponseTypeMap ;

    public ProductImage fromRequestToEntity (ProductImageDTO productImageDTO) {
        if(productImageDTO == null) return null ;
        if(fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = this.modelMapper.createTypeMap(ProductImageDTO.class , ProductImage.class) ;
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> mapper.skip(ProductImage :: setProduct));
            fromRequestToEntityTypeMap.implicitMappings();
        }

        ProductImage productImage = fromRequestToEntityTypeMap.map(productImageDTO);

        // map product
        if(productImageDTO.getProductId() != null) {
            Product product = this.productRepository.findById(productImageDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Sản phẩm với id " + productImageDTO.getProductId() + " không tồn tại"));
            productImage.setProduct(product);
        }
        return productImage ;
    }

    public ProductImageResponse fromEntityToResponse (ProductImage productImage){
        if(productImage == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(ProductImage.class , ProductImageResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> mapper.map(src -> src.getProduct().getId() , ProductImageResponse :: setProductId));
            fromEntityToResponseTypeMap.implicitMappings();
        }
        ProductImageResponse productImageResponse = fromEntityToResponseTypeMap.map(productImage) ;

        // map product id
        if(productImage.getProduct() != null){
            productImageResponse.setProductId(productImage.getProduct().getId());
        }
        return productImageResponse ;
    }
}
