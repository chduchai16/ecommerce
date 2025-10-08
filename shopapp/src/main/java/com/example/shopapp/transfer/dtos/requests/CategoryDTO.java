package com.example.shopapp.transfer.dtos.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @Nullable
    private Integer id ;

    @NotNull(message = "Tên danh mục không được để trống")
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name ;
}
