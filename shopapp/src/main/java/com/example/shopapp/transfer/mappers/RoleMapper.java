package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Role;
import com.example.shopapp.transfer.dtos.requests.RoleDTO;
import com.example.shopapp.transfer.dtos.responses.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final ModelMapper modelMapper ;
    private TypeMap<RoleDTO, Role> fromRequestToEntityTypeMap ;
    private TypeMap<Role , RoleResponse> fromEntityToResponseTypeMap ;

    public Role fromRequestToEntity (RoleDTO roleDTO) {
        if (roleDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(RoleDTO.class , Role.class) ;
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.implicitMappings();
        }

        return fromRequestToEntityTypeMap.map(roleDTO) ;
    }

    public RoleResponse fromEntityToResponse (Role role){
        if(role == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Role.class , RoleResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.implicitMappings();
        }
        return fromEntityToResponseTypeMap.map(role) ;
    }
}
