package com.example.exona_tech.dtos.resposnes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private int id ;

    private String name ;

    private String description ;

    private Float price ;

    @JsonProperty("stock_quantity")
    private int stockQuantity ;

    @JsonProperty("category_name")
    private String categoryName ;

    private String brand;

    @JsonProperty("average_rating")
    private Float averageRating;

    private String thumbnail ;
    private Long views ;

    @JsonProperty("product_images")
    List<ProductImageResponse> productImageResponses;
}
