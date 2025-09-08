package com.example.exona_tech.dtos.resposnes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponse {

    private int id ;

    private String name ;

    private String description ;

    private Float price ;

    @JsonProperty("stock_quantity")
    private int stockQuantity ;

    @JsonProperty("category_response")
    private CategoryResponse categoryResponse ;

    private String brand;

    @JsonProperty("average_rating")
    private Float averageRating;

    private String thumbnail ;
    private Long views ;

    @JsonProperty("product_images")
    List<ProductImageResponse> productImageResponses;
}
