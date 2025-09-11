package com.example.domain.specifications;

import com.example.domain.entities.Category;
import com.example.domain.entities.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasName (String keyword){
        return (root, query , cb) -> {
            if (keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")) , "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasCategoryName (String keyword) {
        return (root, query , cb) -> {
            if(keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            Join<Category , Product> categoryJoin = root.join("category" , JoinType.INNER);

            return cb.like(cb.lower(root.get("category").get("name")) , "%" + keyword.toLowerCase() + "%");
        };
    }

}

