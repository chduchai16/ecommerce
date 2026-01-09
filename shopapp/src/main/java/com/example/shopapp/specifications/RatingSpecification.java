package com.example.shopapp.specifications;

import com.example.shopapp.models.entities.Rating;
import org.springframework.data.jpa.domain.Specification;

public class RatingSpecification {

    public static Specification<Rating> hasProductId(Integer productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("product").get("id"), productId);
        };
    }

    public static Specification<Rating> hasUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Rating> hasProductName(String productName) {
        return (root, query, criteriaBuilder) -> {
            if (productName == null || productName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), "%" + productName.toLowerCase() + "%");
        };
    }

    public static Specification<Rating> hasUserName(String userName) {
        return (root, query, criteriaBuilder) -> {
            if (userName == null || userName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("username")), "%" + userName.toLowerCase() + "%");
        };
    }

    public static Specification<Rating> hasMinRate(Integer minRate) {
        return (root, query, criteriaBuilder) -> {
            if (minRate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), minRate);
        };
    }

    public static Specification<Rating> hasMaxRate(Integer maxRate) {
        return (root, query, criteriaBuilder) -> {
            if (maxRate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("rate"), maxRate);
        };
    }

    public static Specification<Rating> hasRateBetween(Integer minRate, Integer maxRate) {
        return (root, query, criteriaBuilder) -> {
            if (minRate == null && maxRate == null) {
                return criteriaBuilder.conjunction();
            }
            if (minRate != null && maxRate != null) {
                return criteriaBuilder.between(root.get("rate"), minRate, maxRate);
            } else if (minRate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), minRate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("rate"), maxRate);
            }
        };
    }

    public static Specification<Rating> hasComment(String comment) {
        return (root, query, criteriaBuilder) -> {
            if (comment == null || comment.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), "%" + comment.toLowerCase() + "%");
        };
    }

}
