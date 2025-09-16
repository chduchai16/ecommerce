package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Coupon;
import com.example.domain.models.enums.CouponStatus;
import com.example.domain.persistence.repositories.CouponRepository;
import com.example.domain.persistence.specifications.CouponSpecification;
import com.example.domain.services.ICouponService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<Coupon> filterCoupon(
            Float minDiscountPercent ,
            Boolean isExpired ,
            String code ,
            Pageable pageable
    ) {
        Specification<Coupon> spec = Specification.where(CouponSpecification.hasCode(code))
                .and(CouponSpecification.isExpired(isExpired))
                .and(CouponSpecification.isGreaterThanOrEqualTo(minDiscountPercent)) ;
        return couponRepository.findAll(spec,pageable) ;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) throws Exception {
        Specification<Coupon> spec = Specification.where(
                CouponSpecification.hasExactCode(coupon.getCode())
        );

        // kiểm tra coupon đã tồn tại chưa bằng spec
        boolean exists = couponRepository.findOne(spec).isPresent();

        if (exists) {
            throw new DataIntegrityViolationException("Mã giảm giá đã tồn tại");
        }
        // validate
        coupon.setStatus(CouponStatus.ACTIVE.ordinal());

        if (coupon.getExpirationDate().isBefore(LocalDate.now())) {
            throw new Exception("Ngày hết hạn phải sau ngày hiện tại");
        }

        if (coupon.getDiscountMoney() == null && coupon.getDiscountPercent() == null) {
            throw new Exception("Phải có ít nhất một hình thức giảm giá");
        }

        if (coupon.getDiscountMoney() != null && coupon.getDiscountPercent() != null) {
            throw new Exception("Chỉ được chọn một hình thức giảm giá");
        }

        return this.couponRepository.save(coupon);
    }

    // apply coupon
    @Override
    public Coupon applyCoupon(String code) throws Exception {
        Specification<Coupon> spec = Specification.where(
                CouponSpecification.hasExactCode(code)
        ) ;

        Optional<Coupon> existingCoupon = couponRepository.findOne(spec) ;
        if(existingCoupon.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy mã giảm giá này") ;
        }
        LocalDate currentDate = LocalDate.now() ;
        if(existingCoupon.get().getExpirationDate().isBefore(currentDate)) {
            throw new Exception ("Mã giảm giá này đã hết hạn") ;
        }
        return existingCoupon.get() ;
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) throws Exception {
        if(coupon.getId() == null){
            throw new Exception("Mã giảm giá không được để trống khi cập nhật");
        }
        Optional<Coupon> existingCoupon = couponRepository.findById(coupon.getId());

        if(existingCoupon.isEmpty()){
            throw new EntityNotFoundException("Mã giảm giá này không tồn tại");
        }
        if(!existingCoupon.get().getCode().equals(coupon.getCode())) {
            throw new DataIntegrityViolationException("Mã giảm giá đã tồn tại") ;
        }
        if(coupon.getExpirationDate().isBefore(LocalDate.now())){
            throw new Exception("Ngày hết hạn phải sau ngày hiện tại");
        }
        if(coupon.getDiscountMoney() == null && coupon.getDiscountPercent() == null){
            throw new Exception("Phải có ít nhất một hình thức giảm giá");
        }
        if(coupon.getDiscountMoney() != null && coupon.getDiscountPercent() != null) {
            throw new Exception("Chỉ được chọn một hình thức giảm giá");
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
