package com.example.shopapp.transfer.mappers;

import com.example.shopapp.models.entities.Product;
import com.example.shopapp.models.entities.ProductSpecification;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.transfer.dtos.requests.ProductSpecificationDTO;
import com.example.shopapp.transfer.dtos.responses.ProductSpecificationResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSpecificationMapper {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    private TypeMap<ProductSpecificationDTO, ProductSpecification> fromRequestToEntityTypeMap;
    private TypeMap<ProductSpecification, ProductSpecificationResponse> fromEntityToResponseTypeMap;

    public ProductSpecification fromRequestToEntity(ProductSpecificationDTO dto) {
        if (dto == null)
            return null;

        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(ProductSpecificationDTO.class,
                    ProductSpecification.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> mapper.skip(ProductSpecification::setProduct));
            fromRequestToEntityTypeMap.implicitMappings();
        }

        ProductSpecification specification = fromRequestToEntityTypeMap.map(dto);

        // Map product
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Sản phẩm với id " + dto.getProductId() + " không tồn tại"));
            specification.setProduct(product);
        }

        return specification;
    }

    public ProductSpecificationResponse fromEntityToResponse(ProductSpecification specification) {
        if (specification == null)
            return null;

        if (fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(ProductSpecification.class,
                    ProductSpecificationResponse.class);
            fromEntityToResponseTypeMap.implicitMappings();
        }

        return fromEntityToResponseTypeMap.map(specification);
    }
}