package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

    private int id ;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("user_id")
    private int userId;

    private int rate ;

    private String comment ;
}
