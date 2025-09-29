package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    @Nullable
    private Integer id ;

    @JsonProperty("user_id")
    private Integer userId ;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItemDTOs ;

}
