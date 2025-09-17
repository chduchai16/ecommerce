package com.example.domain.persistence.specifications;

import com.example.domain.models.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification {

    // lọc nhà cung cấp theo id , tên , số điện thoại , email , địa chỉ
    public static Specification<Supplier> hasId (Integer id ) {
        return (root, query , cb) -> {
            if(id == null) {
                return cb.conjunction() ;
            }
            return cb.equal(root.get("id") , id) ;
        } ;
    }

    public static Specification<Supplier> hasName (String name ) {
        return (root, query , cb) -> {
            if(name == null || name.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.like(cb.lower(root.get("name")) , "%" + name.toLowerCase() + "%") ;
        } ;
    }

    public static Specification<Supplier> hasExactName (String name ) {
        return (root, query , cb) -> {
            if(name == null || name.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.equal(cb.lower(root.get("name")) , name.toLowerCase()) ;
        } ;
    }

    public static Specification<Supplier> hasPhoneNumber (String phoneNumber ) {
        return (root, query , cb) -> {
            if(phoneNumber == null || phoneNumber.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.like(cb.lower(root.get("phoneNumber")) , "%" + phoneNumber.toLowerCase() + "%") ;
        } ;
    }

    public static Specification<Supplier> hasExactPhoneNumber (String phoneNumber ) {
        return (root, query , cb) -> {
            if(phoneNumber == null || phoneNumber.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.equal(cb.lower(root.get("phoneNumber")) , phoneNumber.toLowerCase()) ;
        } ;
    }

    public static Specification<Supplier> hasEmail (String email ) {
        return (root, query , cb) -> {
            if(email == null || email.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.like(cb.lower(root.get("email")) , "%" + email.toLowerCase() + "%") ;
        } ;
    }

    public static Specification<Supplier> hasExactEmail (String email ) {
        return (root, query , cb) -> {
            if(email == null || email.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.equal(cb.lower(root.get("email")) , email.toLowerCase()) ;
        } ;
    }

    public static Specification<Supplier> hasAddress (String address ) {
        return (root, query , cb) -> {
            if(address == null || address.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.like(cb.lower(root.get("address")) , "%" + address.toLowerCase() + "%") ;
        } ;
    }

    public static Specification<Supplier> hasExactAddress (String address ) {
        return (root, query , cb) -> {
            if(address == null || address.isEmpty()) {
                return cb.conjunction() ;
            }
            return cb.equal(cb.lower(root.get("address")) , address.toLowerCase()) ;
        } ;
    }
}
