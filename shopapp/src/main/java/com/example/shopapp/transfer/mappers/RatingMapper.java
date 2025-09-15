package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Product;
import com.example.domain.models.entities.Rating;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.ProductRepository;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.shopapp.transfer.dtos.requests.RatingDTO;
import com.example.shopapp.transfer.dtos.responses.RatingResponse;
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

    private TypeMap<RatingDTO, Rating> fromRequestToEntityTypeMap ;
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
            Product product = productRepository.findById(ratingDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Sản phẩm với id " + ratingDTO.getProductId() + " không tồn tại"));
            rating.setProduct(product);
        }

        // set user
        if (ratingDTO.getUserId() != null) {
            User user = userRepository.findById(ratingDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Người dùng với id " + ratingDTO.getUserId() + " không tồn tại"));
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
