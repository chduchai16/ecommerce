package com.example.shopapp.specifications;

import com.example.shopapp.models.entities.Coupon;
import org.springframework.data.jpa.domain.Specification;

public class CouponSpecification {

    public static Specification<Coupon> hasCode(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("code")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Coupon> hasExactCode(String code) {
        return (root, query, cb) -> {
            if (code == null || code.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("code")), code.toLowerCase());
        };
    }

    public static Specification<Coupon> isGreaterThanOrEqualTo(Float minDiscountPercent) {
        return (root, query, cb) -> {
            if (minDiscountPercent == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("discountPercent"), minDiscountPercent);
        };
    }

    public static Specification<Coupon> isExpired(Boolean isExpired) {
        return (root, query, cb) -> {
            if (isExpired == null) {
                return cb.conjunction();
            }
            if (isExpired) {
                return cb.lessThan(root.get("expiryDate"), cb.currentDate());
            } else {
                return cb.greaterThanOrEqualTo(root.get("expiryDate"), cb.currentDate());
            }
        };
    }
}
