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

    @NotNull(message = "Role id must not be null")
    @NotBlank(message = "Role name must not be blank")
    private String name ;
}
