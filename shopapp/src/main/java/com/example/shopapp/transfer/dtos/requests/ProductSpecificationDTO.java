package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDTO {

    private Integer id;

    @JsonProperty("product_id")
    private Integer productId;

    @NotBlank(message = "Khóa thông số không được để trống")
    @JsonProperty("spec_key")
    private String key;

    @NotBlank(message = "Giá trị thông số không được để trống")
    @JsonProperty("spec_value")
    private String value;
}