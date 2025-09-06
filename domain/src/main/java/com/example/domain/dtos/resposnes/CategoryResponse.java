package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private int id ;
    private String name ;

    public static CategoryResponse convertFromCategory(Category category){
        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return categoryResponse ;
    }
}
