package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Coupon;
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

    public static CouponResponse convertFromCoupon(Coupon coupon){
        CouponResponse couponResponse = CouponResponse
                .builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountMoney(coupon.getDiscountMoney())
                .discountPercent(coupon.getDiscountPercent())
                .expirationDate(coupon.getExpirationDate())
                .build();
        return couponResponse ;
    }
}
