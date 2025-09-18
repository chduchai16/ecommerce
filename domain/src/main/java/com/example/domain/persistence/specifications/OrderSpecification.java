package com.example.domain.persistence.specifications;

import com.example.domain.models.entities.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    // lọc theo id
    public static Specification<Order> hasId(Integer id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    // lọc theo userId
    public static Specification<Order> hasUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Order> minTotalAmount(Float minTotalAmount) {
        return (root, query, criteriaBuilder) -> {
            if (minTotalAmount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), minTotalAmount);
        };
    }

    public static Specification<Order> maxTotalAmount(Float maxTotalAmount) {
        return (root, query, criteriaBuilder) -> {
            if (maxTotalAmount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), maxTotalAmount);
        };
    }

    public static Specification<Order> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Order> hasShippingAddress(String shippingAddress) {
        return (root, query, criteriaBuilder) -> {
            if (shippingAddress == null || shippingAddress.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("shippingAddress")), "%" + shippingAddress.toLowerCase() + "%");
        };
    }

    public static Specification<Order> hasCustomerName(String customerName) {
        return (root, query, criteriaBuilder) -> {
            if (customerName == null || customerName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%");
        };
    }

}
