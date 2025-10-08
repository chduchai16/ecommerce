package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    @Nullable
    private Integer id ;

    @JsonProperty("product_id")
    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;

    @JsonProperty("image_name")
    private String imageName ;
}
