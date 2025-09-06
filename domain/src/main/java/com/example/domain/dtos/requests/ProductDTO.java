package com.example.domain.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @Nullable
    private Integer id ;

    @NotNull(message = "Name must not be null.")
    @Size(min =  5 , max = 255 , message = "Name must from 5 to 255 chars.")
    private String name ;

    private String description;

    @Min(value = 0 , message = "Product's price must not be < 0.")
    private Float price ;

    @JsonProperty("stock_quantity")
    private int stockQuantity;

    @JsonProperty("category_id")
    private Integer categoryId ;

    private String brand;

    @JsonProperty("supplier_id")
    @Nullable
    private Integer supplierId ;

    @JsonProperty("cart_item_ids")
    private List<Integer> cartItemIds ;

    @JsonProperty("product_image_ids")
    private List<Integer> productImageIds;

    private String thumbnail ;

    private Long views ;

}
