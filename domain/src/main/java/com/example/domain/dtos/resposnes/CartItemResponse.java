package com.example.domain.dtos.resposnes;

import com.example.domain.entities.CartItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private int id ;

    @JsonProperty("product_response")
    private ProductResponse productResponse ;

    private int quantity ;

    public static CartItemResponse converCartItemToCartItemResponse(CartItem cartItem){
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .productResponse(ProductResponse.convertFromProduct(cartItem.getProduct()))
                .build();
    }
}
