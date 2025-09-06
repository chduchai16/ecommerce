package com.example.domain.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    @Nullable
    private Integer id ;

    @JsonProperty("product_id")
    @NotNull
    private Integer productId ;

    @JsonProperty("cart_id")
    private Integer cartId ;

    @NotNull
    @Min(value = 1, message = "quantity must be > 0")
    private Integer quantity;
}
