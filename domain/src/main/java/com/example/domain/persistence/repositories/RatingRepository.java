package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> , JpaSpecificationExecutor<Rating> {
}
