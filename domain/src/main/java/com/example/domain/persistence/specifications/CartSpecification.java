package com.example.domain.persistence.specifications;

import com.example.domain.models.entities.Cart;
import org.springframework.data.jpa.domain.Specification;

public class CartSpecification {

    public static Specification<Cart> hasId(Integer id) {
        return (root, query, cb) -> {
            if(id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<Cart> hasUserId(Integer userId) {
        return (root, query, cb) -> {
            if(userId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }


}
