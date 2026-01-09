package com.example.shopapp.specifications;

import com.example.shopapp.models.entities.Product;
import com.example.shopapp.models.entities.ProductImage;
import org.springframework.data.jpa.domain.Specification;

public class ProductImageSpecification {

    public static Specification<ProductImage> hasId (Integer id) {
        return (root , query , cb) -> {
            if(id == null) {
                cb.conjunction();
            }
            return cb.equal(root.get("id") , id);
        } ;
    }

    public static Specification<Product> hasProductId (Integer productId) {
        return (root , query , cb) -> {
            if(productId == null) {
                cb.conjunction();
            }
            return cb.equal(root.get("product").get("id") , productId);
        } ;
    }

    public static Specification<ProductImage> hasImageName (String imageName) {
        return (root , query , cb) -> {
            if(imageName == null) {
                cb.conjunction();
            }
            return cb.like(root.get("imageName") , "%" + imageName + "%");
        } ;
    }

}
