package com.example.shopapp.specifications;

import com.example.shopapp.models.entities.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<Category> hasName (String name) {
        return (root , query , cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("name")), name.toLowerCase());
        } ;
    }

    public static Specification<Category> hasExactName (String name) {
        return (root , query , cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), name);
        } ;
    }

}
