package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.RoleDTO;
import com.example.domain.entities.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final ModelMapper modelMapper ;
    private TypeMap<RoleDTO , Role> fromRequestToEntityTypeMap ;

    public Role fromRequestToEntity (RoleDTO roleDTO) {
        if (roleDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(RoleDTO.class , Role.class) ;
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.implicitMappings();
        }

        return fromRequestToEntityTypeMap.map(roleDTO) ;
    }
}
