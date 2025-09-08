package com.example.exona_tech.dtos.resposnes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponse {

    private int id ;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("user_id")
    private int userId;

    private int rate ;

    private String comment ;
}
