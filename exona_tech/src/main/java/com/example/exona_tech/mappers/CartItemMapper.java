package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.CartItemDTO;
import com.example.domain.dtos.resposnes.CartItemResponse;
import com.example.domain.dtos.resposnes.ProductResponse;
import com.example.domain.entities.Cart;
import com.example.domain.entities.CartItem;
import com.example.domain.entities.Product;
import com.example.domain.repositories.CartRepository;
import com.example.domain.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CartItemMapper {
    private final CartRepository cartRepository ;
    private final ProductRepository productRepository ;
    private final ModelMapper modelMapper ;
    private final ProductMapper productMapper ;

    private TypeMap<CartItemDTO , CartItem> fromRequestToEntityTypeMap ;
    private TypeMap<CartItem , CartItemResponse> fromEntityToResponseTypeMap ;

    public CartItem fromRequestToEntity (CartItemDTO cartItemDTO) {
        if(cartItemDTO == null) return null ;
        if(fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(CartItemDTO.class , CartItem.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(CartItem :: setCart);
                mapper.skip(CartItem :: setProduct);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        CartItem cartItem = fromRequestToEntityTypeMap.map(cartItemDTO);

        // map cart
        if(cartItemDTO.getCartId() != null) {
            Cart cart = this.cartRepository.findById(cartItemDTO.getCartId()).orElseThrow(()-> new EntityNotFoundException("This cart does not exist"));
            cartItem.setCart(cart);
        }
        // map product
        if(cartItemDTO.getProductId() != null) {
            Product product = this.productRepository.findById(cartItemDTO.getProductId()).orElseThrow(()-> new EntityNotFoundException("This product does not exist"));
            cartItem.setProduct(product);
        }

        return cartItem ;
    }

    public CartItemResponse fromEntityToResponse (CartItem cartItem) {
        if(cartItem == null) return null ;
        if(fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(CartItem.class , CartItemResponse.class) ;
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip( CartItemResponse :: setProductResponse);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }
        CartItemResponse cartItemResponse = fromEntityToResponseTypeMap.map(cartItem) ;

        // map product response
        if(cartItem.getProduct() != null) {
            ProductResponse productResponse = productMapper.fromEntityToResponse(cartItem.getProduct());
            cartItemResponse.setProductResponse(productResponse);
        }

        return cartItemResponse ;
    }
}
