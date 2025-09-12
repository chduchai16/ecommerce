package com.example.domain.specifications;

import com.example.domain.entities.Category;
import com.example.domain.entities.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    // tên hàng hóa
    public static Specification<Product> hasName (String keyword){
        return (root, query , cb) -> {
            if (keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")) , "%" + keyword.toLowerCase() + "%");
        };
    }

    // tên danh mục
    public static Specification<Product> hasCategoryName (String keyword) {
        return (root, query , cb) -> {
            if(keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            Join<Category , Product> categoryJoin = root.join("category" , JoinType.INNER);

            return cb.like(cb.lower(root.get("category").get("name")) , "%" + keyword.toLowerCase() + "%");
        };
    }

    // màu sắc
    public static Specification<Product> hasColor (String keyword) {
        return (root,query , cb) -> {
            if(keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("color")) , "%" + keyword.toLowerCase() + "%");
        } ;
    }

    // thương hiệu
    public static Specification<Product> hasBrand (String keyword) {
        return (root,query , cb) -> {
            if(keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("brand")) , "%" + keyword.toLowerCase() + "%");
        } ;
    }

    // giá từ - đến
    public static Specification<Product> hasPriceBetween (Float minPrice , Float maxPrice) {
        return (root,query , cb) -> {
            if(minPrice == null && maxPrice == null){
                return cb.conjunction();
            }
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        } ;
    }

    // số lượng tồn kho lớn hơn
    public static Specification<Product> hasStockQuantityGreaterThan (Integer quantity) {
        return (root,query , cb) -> {
            if(quantity == null){
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("stockQuantity") , quantity);
        } ;
    }

    // đánh giá trung bình lớn hơn
    public static Specification<Product> hasAverageRatingGreaterThan (Float rating) {
        return (root,query , cb) -> {
            if(rating == null){
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("averageRating") , rating);
        } ;
    }

    public static Specification<Product> hasDescription (String keyword) {
        return (root, query , cb) -> {
            if (keyword == null || keyword.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("description")) , "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasMoreViewsThan (Long views) {
        return (root,query , cb) -> {
            if(views == null){
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("views") , views);
        } ;
    }
}

