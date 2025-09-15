package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Supplier;
import com.example.shopapp.transfer.dtos.requests.SupplierDTO;
import com.example.shopapp.transfer.dtos.responses.SupplierResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierMapper {
    private final ModelMapper modelMapper ;
    private TypeMap<SupplierDTO, Supplier> fromRequestToEntityTypeMap ;
    private TypeMap<Supplier , SupplierResponse> fromEntityToResponseTypeMap ;

    public Supplier fromRequestToEntity (SupplierDTO supplierDTO) {
        if(supplierDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(SupplierDTO.class , Supplier.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.implicitMappings();
        }

        return fromRequestToEntityTypeMap.map(supplierDTO);
    }

    public SupplierResponse fromEntityToResponse (Supplier supplier){
        if(supplier == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Supplier.class , SupplierResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.implicitMappings();
        }
        return fromEntityToResponseTypeMap.map(supplier) ;
    }
}
