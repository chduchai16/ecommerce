package com.example.shopapp.specifications;

import com.example.shopapp.models.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasId(Integer id) {
        return (root, query, cb) -> {
            if(id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<User> notId(Integer id) {
        return (root, query, cb) -> {
            if(id == null) {
                return cb.conjunction();
            }
            return cb.notEqual(root.get("id"), id);
        };
    }

    public static Specification<User> hasName (String name) {
        return (root , query , cb) -> {
            if(name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")) , "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasEmail (String email) {
        return (root , query , cb) -> {
            if(email == null || email.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")) , "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasEmailExact (String email) {
        return (root , query , cb) -> {
            if(email == null || email.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("email")) , email.toLowerCase());
        };
    }

    public static Specification<User> hasPhoneNumber (String phoneNumber) {
        return (root , query , cb) -> {
            if(phoneNumber == null || phoneNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("phoneNumber")) , "%" + phoneNumber.toLowerCase() + "%");
        };
    }
    public static Specification<User> hasPhoneNumberExact (String phoneNumber) {
        return (root , query , cb) -> {
            if(phoneNumber == null || phoneNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("phoneNumber")) , phoneNumber.toLowerCase());
        };
    }

    public static Specification<User> hasAddress(String address) {
        return (root , query , cb) -> {
            if(address == null || address.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("address")) , "%" + address.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasStatus (Integer status) {
        return (root , query , cb) -> {
            if(status == null ) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status") , status);
        };
    }

}
