package com.example.exona_tech.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    @Nullable
    private Integer id ;

    @JsonProperty("product_id")
    @NotNull(message = "Product id must not be null.")
    private Integer productId;

    @JsonProperty("image_name")
    private String imageName ;
}
