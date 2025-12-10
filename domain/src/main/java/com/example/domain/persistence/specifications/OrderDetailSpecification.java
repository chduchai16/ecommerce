package com.example.domain.persistence.specifications;

import com.example.domain.models.entities.OrderDetail;
import org.springframework.data.jpa.domain.Specification;

public class OrderDetailSpecification {

    public static Specification<OrderDetail> hasOrderId(Integer orderId) {
        return (root, query, criteriaBuilder) -> {
            if (orderId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("order").get("id"), orderId);
        };
    }

    public static Specification<OrderDetail> hasId(Integer id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }


    public static Specification<OrderDetail> hasProductId(Integer productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("product").get("id"), productId);
        };
    }
}
