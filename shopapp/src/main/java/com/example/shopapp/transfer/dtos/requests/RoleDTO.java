package com.example.shopapp.transfer.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Integer id ;

    @NotNull(message = "ID vai trò không được để trống")
    @NotBlank(message = "Tên vai trò không được để trống")
    private String name ;
}
