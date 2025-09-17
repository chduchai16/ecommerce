package com.example.domain.services;

import com.example.domain.models.entities.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRatingService {
    Page<Rating> filterRatings (String productName , String userName , Integer minRate , Integer maxRate , String comment , Pageable pageable) ;
    Page<Rating> getRatingsByProductId (int productId, Pageable pageable) ;
    Rating getRatingByProductIdAndUserId(int productId, int userId) throws Exception;
    Rating createRating(Rating rating) throws Exception;
    Rating updateRating(Rating rating) throws Exception;
    void deleteRating(int ratingId) throws Exception;
}
