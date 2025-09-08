package com.example.exona_tech.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    @Nullable
    private Integer id ;

    @NotNull(message = "Product id must not be null")
    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("user_id")
    @NotNull(message = "Product id must not be null")
    private Integer userId;

    @NotNull
    @Max(value = 5,message = "Rating must be < 6")
    @Min(value = 1,message = "Rating must be > 0")
    private int rate ;

    private String comment ;
}
