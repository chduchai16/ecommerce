package com.example.domain.dtos.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    @Nullable
    private Integer id ;

    @NotNull(message = "Role's name must not be null.")
    private String name ;

}
