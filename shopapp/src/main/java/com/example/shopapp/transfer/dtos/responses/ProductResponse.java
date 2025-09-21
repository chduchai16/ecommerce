package com.example.shopapp.transfer.dtos.responses;

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

    private int id;

    private String name;

    private String description;

    private Float price;

    @JsonProperty("original_price")
    private Float originalPrice;

    private Integer discount;

    @JsonProperty("review_count")
    private Integer reviewCount;

    @JsonProperty("in_stock")
    private Boolean inStock;

    private String tags;

    @JsonProperty("stock_quantity")
    private int stockQuantity;

    @JsonProperty("category_name")
    private String categoryName;

    private String brand;

    @JsonProperty("average_rating")
    private Float averageRating;

    private String thumbnail;
    private Long views;

    @JsonProperty("product_images")
    List<ProductImageResponse> productImageResponses;

    private UserResponse seller;

    private List<ProductSpecificationResponse> specifications;
}
