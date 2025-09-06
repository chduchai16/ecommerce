package com.example.domain.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    @Nullable
    private Integer id ;

    @JsonProperty("user_id")
    @NotNull
    private Integer userId ;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItemDTOs ;

}
