package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationResponse {
    private Integer id;

    @JsonProperty("spec_key")
    private String key;

    @JsonProperty("spec_value")
    private String value;
}