package com.example.shopapp.transfer.dtos.responses;

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
}
