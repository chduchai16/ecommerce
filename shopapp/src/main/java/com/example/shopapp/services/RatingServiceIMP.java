package com.example.shopapp.services;

import com.example.shopapp.models.entities.Rating;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.repositories.RatingRepository;
import com.example.shopapp.specifications.RatingSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceIMP implements IRatingService {

    private final RatingRepository ratingRepository ;
    private final ProductRepository productRepository;

    // lấy theo sản phẩm và người dùng
    @Override
    public Rating getRatingByProductIdAndUserId(int productId, int userId) throws Exception {

        Specification<Rating> spec = Specification.where(RatingSpecification.hasProductId(productId))
                .and(RatingSpecification.hasUserId(userId)) ;

        Optional<Rating> existingRating = ratingRepository.findOne(spec);
        if (existingRating.isEmpty()) {
            throw new EntityNotFoundException("Đánh giá không tồn tại");
        }
        return existingRating.get();
    }
    // lọc đánh giá
    @Override
    public Page<Rating> filterRatings(String productName, String userName, Integer minRate, Integer maxRate, String comment , Pageable pageable) {
        Specification<Rating> spec = Specification.where(RatingSpecification.hasProductName(productName))
                .and(RatingSpecification.hasUserName(userName))
                .and(RatingSpecification.hasComment(comment)) ;
        if (minRate != null && maxRate != null) {
            spec = spec.and(RatingSpecification.hasRateBetween(minRate, maxRate));
        } else if (minRate != null) {
            spec = spec.and(RatingSpecification.hasMinRate(minRate));
        } else if (maxRate != null) {
            spec = spec.and(RatingSpecification.hasMaxRate(maxRate));
        }
        return ratingRepository.findAll(spec, pageable);
    }

    // tạo đánh giá
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Rating createRating(Rating rating) throws Exception {
        if(rating.getId() != null) {
            rating.setId(null);
        }
        Specification<Rating> spec = Specification.where(RatingSpecification.hasProductId(rating.getProduct().getId()))
                .and(RatingSpecification.hasUserId(rating.getUser().getId())) ;

        Optional<Rating> existingRating = ratingRepository.findOne(spec);
        if (existingRating.isPresent()) {
            throw new Exception("Người dùng đã đánh giá sản phẩm này");
        }
        return ratingRepository.save(rating);
    }

    @Override
    public Page<Rating> getRatingsByProductId(int productId , Pageable pageable) {
        Specification<Rating> spec = Specification.where(RatingSpecification.hasProductId(productId)) ;
        return ratingRepository.findAll(spec , pageable) ;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Rating updateRating(Rating rating) throws Exception {
        // chỉ update comment và đánh giá
        if (rating.getId() == null) {
            throw new Exception("Id không được để trống khi cập nhật");
        }

        Rating exist = ratingRepository.findById(rating.getId()).orElseThrow(()->new EntityNotFoundException("Đánh giá không tồn tại"));

        if(!Objects.equals(exist.getUser().getId(), rating.getUser().getId()) && !Objects.equals(rating.getProduct().getId(), rating.getProduct().getId())){
            throw new Exception("Người dùng và sản phẩm không khớp");
        }

        exist.setRate(rating.getRate());
        exist.setComment(rating.getComment());
        ratingRepository.save(exist);
        productRepository.updateAverageRating(rating.getProduct().getId());
        return exist ;
    }

    @Override
    public void deleteRating(int ratingId) throws Exception {
        if (ratingRepository.findById(ratingId).isEmpty()){
            throw new EntityNotFoundException("Đánh giá không tồn tại");
        }
        else {
            ratingRepository.deleteById(ratingId);
        }
    }
}
