package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {

    private int id ;
    private String name ;

    public static RoleResponse convertFromRole(Role role){
        RoleResponse roleResponse = RoleResponse
                .builder()
                .id(role.getId())
                .name(role.getName())
                .build();
        return roleResponse;
    }
}
