package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.ProductDTO;
import com.example.domain.dtos.requests.RatingDTO;
import com.example.domain.dtos.resposnes.RatingResponse;
import com.example.domain.entities.Product;
import com.example.domain.entities.Rating;
import com.example.domain.entities.User;
import com.example.domain.repositories.ProductRepository;
import com.example.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper {
    private final ModelMapper modelMapper ;
    private final ProductRepository productRepository ;
    private final UserRepository userRepository ;

    private TypeMap<RatingDTO , Rating> fromRequestToEntityTypeMap ;
    private TypeMap<Rating , RatingResponse> fromEntityToResponseTypeMap ;

    public Rating fromRequestToEntity(RatingDTO ratingDTO) {
        if(ratingDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(RatingDTO.class , Rating.class) ;
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(Rating :: setUser);
                mapper.skip(Rating :: setProduct);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        Rating rating = fromRequestToEntityTypeMap.map(ratingDTO);
        // set product
        if(ratingDTO.getProductId() != null) {
            Product product = productRepository.findById(ratingDTO.getProductId()).orElseThrow(()-> new EntityNotFoundException("This product does not exist"));
            rating.setProduct(product);
        }

        // set user
        if (ratingDTO.getUserId() != null) {
            User user = userRepository.findById(ratingDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("This user does not exist"));
            rating.setUser(user);
        }
        return rating ;
    }

    public RatingResponse fromEntityToResponse(Rating rating){
        if(rating == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Rating.class , RatingResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(RatingResponse :: setUserId);
                mapper.skip(RatingResponse :: setProductId);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }
        RatingResponse ratingResponse = fromEntityToResponseTypeMap.map(rating) ;

        // map user id
        if(rating.getUser() != null){
            ratingResponse.setUserId(rating.getUser().getId());
        }
        // map product id
        if(rating.getProduct() != null){
            ratingResponse.setProductId(rating.getProduct().getId());
        }
        return ratingResponse ;
    }
}
