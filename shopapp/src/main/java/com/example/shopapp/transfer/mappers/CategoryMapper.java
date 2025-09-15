package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Category;
import com.example.shopapp.transfer.dtos.requests.CategoryDTO;
import com.example.shopapp.transfer.dtos.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper ;
    private TypeMap <CategoryDTO, Category> fromRequestToEntityTypeMap ;
    private TypeMap<Category , CategoryResponse> fromEntityToResponseTypeMap;

    public Category fromRequestToEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) return null ;
        if (fromRequestToEntityTypeMap== null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(CategoryDTO.class , Category.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.implicitMappings();
        }
        return fromRequestToEntityTypeMap.map(categoryDTO) ;
    }

    public CategoryResponse fromEntityToResponse (Category category){
        if(category == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Category.class , CategoryResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.implicitMappings();
        }
        return fromEntityToResponseTypeMap.map(category) ;
    }
}
