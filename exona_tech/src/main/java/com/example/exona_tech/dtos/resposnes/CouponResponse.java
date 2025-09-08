package com.example.exona_tech.dtos.resposnes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CouponResponse {

    private int id ;

    private String code ;

    @JsonProperty("discount_percent")
    private Float discountPercent;

    @JsonProperty("discount_money")
    private Float discountMoney ;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate ;
}
