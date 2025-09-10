package com.example.exona_tech.imp_services;

import com.example.domain.entities.Coupon;
import com.example.domain.repositories.CouponRepository;
import com.example.domain.services.ICouponService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceIMP implements ICouponService {

    private final CouponRepository couponRepository ;

    @Override
    public Coupon getCouponById(int couponId) throws Exception {
        return couponRepository.findById(couponId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy mã giảm giá này")) ;
    }


    // apply coupon
    @Override
    public Coupon applyCoupon(String code) throws Exception {
        if(couponRepository.findByCode(code).isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy mã giảm giá này") ;
        }
        Coupon existingCoupon = couponRepository.findByCode(code).get() ;
        LocalDate currentDate = LocalDate.now() ;
        if(existingCoupon.getExpirationDate().isBefore(currentDate)) {
            throw new Exception ("Mã giảm giá này đã hết hạn") ;
        }
        return existingCoupon ;
    }

    @Override
    public Page<Coupon> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    @Override
    public Coupon createCoupon(Coupon coupon) throws Exception {
        if (couponRepository.findByCode(coupon.getCode()).isEmpty()){
            return this.couponRepository.save(coupon);
        }
        else {
            throw new DataIntegrityViolationException("Mã giảm giá đã tồn tại") ;
        }

    }

    @Override
    public Coupon updateCoupon(Coupon coupon) throws Exception {
        if(coupon.getId() == null){
            return null;
        }
        Optional<Coupon> existingCoupon = couponRepository.findById(coupon.getId());
        if(existingCoupon.isEmpty()){
            throw new EntityNotFoundException("Mã giảm giá này không tồn tại");
        }
        if(couponRepository.findByCode(coupon.getCode()).isPresent() && !existingCoupon.get().getCode().equals(coupon.getCode())) {
            throw new DataIntegrityViolationException("Mã giảm giá đã tồn tại") ;
        }
        return couponRepository.save(coupon) ;
    }

    @Override
    public void deleteCoupon(int couponId) throws Exception {
        if(couponRepository.findById(couponId).isEmpty()){
           throw new EntityNotFoundException("Mã giảm giá này không tồn tại");
        }
        couponRepository.deleteById(couponId);
    }
}
