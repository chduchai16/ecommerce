package com.example.domain.dtos.resposnes;

import com.example.domain.entities.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageResponse {
    private int id ;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("image_name")
    private String imageName ;

    public static ProductImageResponse convertFromProductImage(ProductImage productImage){
        ProductImageResponse productImageResponse = ProductImageResponse
                .builder()
                .id(productImage.getId())
                .productId(productImage.getProduct().getId())
                .imageName(productImage.getImageName())
                .build();
        return productImageResponse ;
    }
}
