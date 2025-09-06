package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Product;
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

    public static ProductResponse convertFromProduct(Product product) {
        List<ProductImageResponse> productImageResponses = product.getProductImages()
                .stream()
                .map(ProductImageResponse::convertFromProductImage)
                .toList() ;
        ProductResponse productResponse = ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .categoryResponse(CategoryResponse.convertFromCategory(product.getCategory()))
                .brand(product.getBrand())
                .averageRating(product.getAverageRating())
                .thumbnail(product.getThumbnail())
                .views(product.getViews())
                .productImageResponses(productImageResponses)
                .build();
        return productResponse;
    }
}
