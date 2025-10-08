package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @Nullable
    private Integer id;

    @NotNull(message = "Tên sản phẩm không được để trống")
    @Size(min = 5, max = 255, message = "Tên sản phẩm phải từ 5 đến 255 ký tự")
    private String name;

    private String description;

    @Min(value = 0, message = "Giá sản phẩm không được nhỏ hơn 0")
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

    @JsonProperty("category_id")
    private Integer categoryId;

    private String brand;

    @JsonProperty("seller_id")
    @Nullable
    private Integer sellerId;

    @JsonProperty("cart_item_ids")
    private List<Integer> cartItemIds;

    @JsonProperty("product_image_ids")
    private List<Integer> productImageIds;

    private String thumbnail;

    private Long views;

}
