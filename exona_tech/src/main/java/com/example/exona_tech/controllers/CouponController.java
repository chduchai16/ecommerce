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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Áp dụng mã giảm giá thành công" , couponResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(EntityNotFoundException e){
            System.out.println("Lỗi lấy mã giảm giá: "+e.getMessage() );
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse) ;
        }
        catch(Exception exception){
            System.out.println("Lỗi lấy mã giảm giá: "+exception.getMessage() );
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
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
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Lấy danh sách mã giảm giá thành công." , pagedCouponsResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Lỗi lấy danh sách mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy thông tin coupong theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponWithId(@PathVariable("id") int couponId){
        try {
            Coupon coupon = couponService.getCouponById(couponId) ;
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Lấy thông tin mã giảm giá thành công." ,couponResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e) {
            System.out.println("Lỗi lấy thông tin mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e) {
            System.out.println("Lỗi lấy thông tin mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + e.getMessage()) ;
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
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Dữ liệu không hợp lệ.") ;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Coupon coupon = couponService.createCoupon(couponMapper.fromRequestToEntity(couponDTO));
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Tạo mã giảm giá thành công." ,couponResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (DataIntegrityViolationException e) {
            System.out.print("Lỗi tạo mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("409" , e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        }
        catch (Exception e) {
            System.out.print("Lỗi tạo mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Coupon coupon = couponService.updateCoupon(couponMapper.fromRequestToEntity(couponDTO));
            CouponResponse couponResponse = couponMapper.fromEntityToResponse(coupon) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Cập nhật mã giảm giá thành công." , couponResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e) {
            System.out.print("Lỗi cập nhật mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (DataIntegrityViolationException e) {
            System.out.print("Lỗi cập nhật mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("409" , e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        }
        catch (Exception e) {
            System.out.print("Lỗi cập nhật mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // xoá coupon
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("id") int couponId){
        try {
            couponService.deleteCoupon(couponId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Xóa mã giảm giá thành công.") ;
            return ResponseEntity.ok().body(baseResponse);
        }
        catch (EntityNotFoundException e) {
            System.out.print("Lỗi xóa mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e) {
            System.out.print("Lỗi xóa mã giảm giá: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
