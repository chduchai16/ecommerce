package com.example.exona_tech.imp_services;

import com.example.domain.entities.Rating;
import com.example.domain.repositories.ProductRepository;
import com.example.domain.repositories.RatingRepository;
import com.example.domain.repositories.UserRepository;
import com.example.domain.services.IRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RatingServiceIMP implements IRatingService {

    private final RatingRepository ratingRepository ;
    private final ProductRepository productRepository;
    private final UserRepository userRepository ;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Rating createRating(Rating rating) throws Exception {
        if(rating.getId() != null) {
            rating.setId(null);
        }
        Rating saved = new Rating();
        if (ratingRepository.findByProductIdAndUserId(rating.getProduct().getId(),rating.getUser().getId()).isEmpty()){
            saved = ratingRepository.save(rating);
            productRepository.updateAverageRating(rating.getProduct().getId());
        }
        return saved ;
    }

    @Override
    public Page<Rating> getRatingsByProductId(int productId , Pageable pageable) {
        return ratingRepository.findByProductId(productId , pageable) ;
    }

    @Override
    public Rating updateRating(Rating rating) throws Exception {
        // chỉ update comment và đánh giá
        if (rating.getId() == null) {
            throw new Exception("Id is must not be null to update");
        }

        Rating exist = ratingRepository.findById(rating.getId()).orElseThrow(()->new Exception("Rating does not exist"));

        if(!Objects.equals(exist.getUser().getId(), rating.getUser().getId()) && !Objects.equals(rating.getProduct().getId(), rating.getProduct().getId())){
            throw new Exception("User & product does not match");
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
            throw new Exception("Rating does not exist");
        }
        else {
            ratingRepository.deleteById(ratingId);
        }
    }

    @Override
    public Rating getRatingByProductIdAndUserId(int productId, int userId) throws Exception {
        return ratingRepository.findByProductIdAndUserId(productId , userId).orElseThrow(()->new Exception("This rating does not exist"));
    }


}
