package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private int id ;
    @JsonProperty("user_id")
    private Integer userId ;
    @JsonProperty("username")
    private String username ;
    @JsonProperty("cart_items")
    private List<CartItemResponse> cartItemResponses = new ArrayList<>();

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CartResponse convertFromCart(Cart cart){

        List<CartItemResponse> cartItemResponseList = cart.getCartItems()
                .stream()
                .map(cartItem -> CartItemResponse.converCartItemToCartItemResponse(cartItem))
                .toList() ;

        CartResponse cartResponse = CartResponse.builder()
                .id(cart.getId())
                .cartItemResponses(cartItemResponseList)
//                .userResponse(UserResponse.convertFromUser(cart.getUser()))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
        return cartResponse ;
    }
}
