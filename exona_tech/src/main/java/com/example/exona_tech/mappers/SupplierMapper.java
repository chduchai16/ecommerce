package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.SupplierDTO;
import com.example.domain.entities.Supplier;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierMapper {
    private final ModelMapper modelMapper ;
    private TypeMap<SupplierDTO , Supplier> fromRequestToEntityTypeMap ;

    public Supplier fromRequestToEntity (SupplierDTO supplierDTO) {
        if(supplierDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(SupplierDTO.class , Supplier.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.implicitMappings();
        }

        return fromRequestToEntityTypeMap.map(supplierDTO);
    }
}
