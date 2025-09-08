package com.example.exona_tech.controllers;

import com.example.exona_tech.dtos.requests.CouponDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.exona_tech.dtos.resposnes.CouponResponse;
import com.example.exona_tech.dtos.resposnes.PagedResponse;
import com.example.domain.entities.Coupon;
import com.example.exona_tech.mappers.CouponMapper;
import com.example.exona_tech.pojos.PaginationInfo;
import com.example.domain.services.ICouponService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/coupons")
@Tag(name = "Coupon Management", description = "Quản lý mã giảm giá trong hệ thống")

public class CouponController {

    private final ICouponService couponService ;
    private final CouponMapper couponMapper ;

    @PostMapping("/apply")
    public ResponseEntity<?> applyCoupon(
            @RequestParam("code") String code
    ){
        try{
            Coupon coupon = couponService.applyCoupon(code);
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Apply coupon successfully" , couponResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(Exception exception){
            System.out.println("Error getting coupon: "+exception.getMessage() );
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Apply coupon failed: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
        }
    }

    // lấy danh sách phân trang coupon
    @GetMapping()
    public ResponseEntity<?> getAllCoupons(
            @RequestParam("page") int page ,
            @RequestParam("limit") int limit
    ){
        try {
            PageRequest pageRequest = PageRequest.of(page , limit) ;
            Page<Coupon> coupons = couponService.getAllCoupons(pageRequest);

            PaginationInfo paginationInfo = new PaginationInfo(
                    coupons.getNumber() ,
                    coupons.getSize() ,
                    coupons.getTotalPages() ,
                    coupons.getTotalElements()
            );
            List<CouponResponse> couponResponses = coupons
                    .stream()
                    .map(couponMapper :: fromEntityToResponse)
                    .toList() ;
            PagedResponse pagedCouponsResponse = new PagedResponse(couponResponses , paginationInfo) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Get coupons successfully." , pagedCouponsResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Error getting coupons: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get coupons failed: " + e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy thông tin coupong theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponWithId(@PathVariable("id") int couponId){
        try {
            Coupon coupon = couponService.getCouponById(couponId) ;
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Get coupon successfully." ,couponResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Error getting coupons: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get coupon failed: " + e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // tạo mới coupon
    @PostMapping()
    public ResponseEntity<?> createCoupon(
            @RequestBody @Valid CouponDTO couponDTO ,
            BindingResult result
    ){
        try {
            if(result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println(errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Invalid data.") ;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Coupon coupon = couponService.createCoupon(couponMapper.fromRequestToEntity(couponDTO));
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Create coupon successfully." ,couponResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.print("Error creating coupon: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Create coupon failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // cập nật coupon
    @PutMapping()
    public ResponseEntity<?> updateCoupon(
            @RequestBody @Valid CouponDTO couponDTO ,
            BindingResult result
    ){
        try {
            if(result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println(errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Coupon coupon = couponService.updateCoupon(couponMapper.fromRequestToEntity(couponDTO));
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Update coupon successfully." , couponResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.print("Error updating coupon: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Update coupon failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // xoá coupon
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("id") int couponId){
        try {
            couponService.deleteCoupon(couponId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Delete coupon successfully.") ;
            return ResponseEntity.ok().body(baseResponse);
        }
        catch (Exception e) {
            System.out.print("Error deleting coupon: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Delete coupon failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
