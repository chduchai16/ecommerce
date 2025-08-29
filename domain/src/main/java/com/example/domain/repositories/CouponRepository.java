package com.example.domain.repositories;

import com.example.domain.entities.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCode(String code) ;
    Optional<Coupon> findByIdAndCode(int id , String code);
    Page<Coupon> findAll(Pageable pageable);

}
