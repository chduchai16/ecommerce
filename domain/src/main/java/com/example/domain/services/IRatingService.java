package com.example.domain.services;

import com.example.domain.entities.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRatingService {
    Rating createRating(Rating rating) throws Exception;
    Page<Rating> getRatingsByProductId (int productId, Pageable pageable) ;
    Rating updateRating(Rating rating) throws Exception;
    void deleteRating(int ratingId) throws Exception;
    Rating getRatingByProductIdAndUserId(int productId, int userId) throws Exception;
}
