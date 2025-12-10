package com.example.shopapp.specifications;

import com.example.shopapp.models.entities.CartItem;
import org.springframework.data.jpa.domain.Specification;

public class CartItemSpecification {

    public static Specification<CartItem> hasId(Integer id) {
        return (root, query, cb) -> {
            if(id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<CartItem> hasCartId(Integer cartId) {
        return (root, query, cb) -> {
            if(cartId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("cart").get("id"), cartId);
        };
    }

}
