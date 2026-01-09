package com.example.shopapp.services;

import com.example.shopapp.models.entities.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICouponService {
    Coupon getCouponById (int couponId) throws Exception;
    Page<Coupon> filterCoupon(Float minDiscountPercent ,Boolean isExpired ,String code ,Pageable pageable);
    Coupon createCoupon(Coupon coupon) throws Exception;
    Coupon updateCoupon(Coupon coupon) throws Exception;
    void deleteCoupon(int couponId) throws Exception;
    Coupon applyCoupon(String code) throws Exception;
}
