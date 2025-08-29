package com.example.domain.repositories;

import com.example.domain.entities.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> {
    Page<Rating> findByProductId(int productId , Pageable pageable);
    Optional<Rating> findByProductIdAndUserId(int productId , int userId) ;
}
