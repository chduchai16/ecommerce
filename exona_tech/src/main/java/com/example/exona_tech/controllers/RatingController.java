package com.example.exona_tech.controllers;

import com.example.domain.dtos.requests.RatingDTO;
import com.example.domain.dtos.resposnes.BaseResponse;
import com.example.domain.dtos.resposnes.PagedResponse;
import com.example.domain.dtos.resposnes.RatingResponse;
import com.example.domain.entities.Rating;
import com.example.domain.pojos.PaginationInfo;
import com.example.domain.services.IProductService;
import com.example.domain.services.IRatingService;
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
@RequestMapping("${app.api-prefix}/ratings")
@Tag(name = "Rating Management", description = "Quản lý đánh giá trong hệ thống")

public class RatingController {

    private final IRatingService ratingService;
    private final IProductService productService;

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
            List<RatingResponse> ratingResponses = ratings.getContent().stream().map(RatingResponse::convertFromRating).toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    ratings.getNumber() ,
                    ratings.getSize() ,
                    ratings.getTotalPages() ,
                    ratings.getTotalElements()
            );

            PagedResponse pagedRatingsResponse = new PagedResponse(ratingResponses , paginationInfo) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get ratings successfully.", pagedRatingsResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting ratings: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get ratings failed.");
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
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get rating successfully.", RatingResponse.convertFromRating(rating));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting rating: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get rating failed.");
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
                System.out.println("Error creating rating: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid rating.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Rating rating = ratingService.createRating(ratingDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Create rating successfully.", RatingResponse.convertFromRating(rating));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error creating rating: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Create rating failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(
            @RequestBody @Valid RatingDTO ratingDTO,
            BindingResult result,
            @PathVariable("id") int ratingId
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
                System.out.println("Error updating rating: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid rating.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Rating rating = ratingService.updateRating(ratingId, ratingDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Update rating successfully.", RatingResponse.convertFromRating(rating));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error updating rating: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Update rating failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(
            @PathVariable("id") int ratingId
    ) {
        try {
            ratingService.deleteRating(ratingId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Delete rating successfully.");
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error deleting rating: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Delete rating failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
