package com.example.shopapp.controllers;

import com.example.domain.models.entities.Rating;
import com.example.domain.services.IProductService;
import com.example.domain.services.IRatingService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.RatingDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.PagedResponse;
import com.example.shopapp.transfer.dtos.responses.RatingResponse;
import com.example.shopapp.transfer.mappers.RatingMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("${app.api-prefix}/ratings")
@Tag(name = "Rating Management", description = "Quản lý đánh giá trong hệ thống")

public class RatingController {

    private final IRatingService ratingService;
    private final IProductService productService;
    private final RatingMapper ratingMapper ;

    // lấy danh sách phân trang nhưgnx đánh giá theo id sản phẩm
    @GetMapping("/product")
    public ResponseEntity<?> getRatingsByProductId(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam("id") int productId
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit);
            Page<Rating> ratings = ratingService.getRatingsByProductId(productId, pageRequest);
            List<RatingResponse> ratingResponses = ratings.getContent()
                    .stream()
                    .map(ratingMapper :: fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    ratings.getNumber() ,
                    ratings.getSize() ,
                    ratings.getTotalPages() ,
                    ratings.getTotalElements()
            );

            PagedResponse pagedRatingsResponse = new PagedResponse(ratingResponses , paginationInfo) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách đánh giá thành công.", pagedRatingsResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/user-product")
    public ResponseEntity<?> getRatingByProductIdAndUserId(
            @RequestParam("product_id") int productId,
            @RequestParam("user_id") int userId
    ) {
        try {
            Rating rating = ratingService.getRatingByProductIdAndUserId(productId, userId);
            RatingResponse ratingResponse = ratingMapper.fromEntityToResponse(rating) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy đánh giá thành công.",ratingResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi lấy đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " +e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createRating(
            @RequestBody @Valid RatingDTO ratingDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Lỗi tạo đánh giá: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Rating rating = ratingService.createRating(ratingMapper.fromRequestToEntity(ratingDTO));
            RatingResponse ratingResponse = ratingMapper.fromEntityToResponse(rating) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tạo đánh giá thành công.",ratingResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi tạo đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateRating(
            @RequestBody @Valid RatingDTO ratingDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Lỗi cập nhật đánh giá: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Rating rating = ratingService.updateRating(ratingMapper.fromRequestToEntity(ratingDTO));
            RatingResponse ratingResponse = ratingMapper.fromEntityToResponse(rating) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Cập nhật đánh giá thành công.",ratingResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi cập nhật đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(
            @PathVariable("id") int ratingId
    ) {
        try {
            ratingService.deleteRating(ratingId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Xóa đánh giá thành công.");
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi xóa đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi xóa đánh giá: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
