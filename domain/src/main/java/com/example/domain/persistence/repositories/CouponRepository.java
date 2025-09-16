package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> , JpaSpecificationExecutor<Coupon> {
}
