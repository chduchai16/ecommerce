package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Supplier;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierResponse {

    private int id ;

    private String name ;

    @JsonProperty("phone_number")
    private String phoneNumber ;

    private String email;

    private String address;

    public static SupplierResponse convertFromSupplier(Supplier supplier) {
        SupplierResponse supplierResponse = SupplierResponse
                .builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .phoneNumber(supplier.getPhoneNumber())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .build();
        return supplierResponse ;
    }
}
