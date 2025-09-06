package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Rating;
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

    public static RatingResponse convertFromRating(Rating rating) {
        RatingResponse ratingResponse = RatingResponse
                .builder()
                .id(rating.getId())
                .productId(rating.getProduct().getId())
                .userId(rating.getUser().getId())
                .rate(rating.getRate())
                .comment(rating.getComment())
                .build();
        return ratingResponse ;
    }
}
