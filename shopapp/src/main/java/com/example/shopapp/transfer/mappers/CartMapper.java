package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Cart;
import com.example.domain.models.entities.CartItem;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.shopapp.transfer.dtos.requests.CartDTO;
import com.example.shopapp.transfer.dtos.responses.CartItemResponse;
import com.example.shopapp.transfer.dtos.responses.CartResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {
    private final UserRepository userRepository ;
    private final ModelMapper modelMapper ;
    private final CartItemMapper cartItemMapper ;

    private TypeMap<CartDTO, Cart> fromRequestToEntityTypeMap ;
    private TypeMap<Cart , CartResponse> fromEntityToResponseTypeMap ;

    public Cart fromRequestToEntity (CartDTO cartDTO) {
        if(cartDTO == null) return null ;
        if(fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = this.modelMapper.createTypeMap(CartDTO.class , Cart.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip( Cart :: setCartItems);
                mapper.skip(Cart :: setUser);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        Cart cart = fromRequestToEntityTypeMap.map(cartDTO);

        // map cart items
        if(cartDTO.getCartItemDTOs() != null && !cartDTO.getCartItemDTOs().isEmpty()) {
            List<CartItem> cartItems = cartDTO.getCartItemDTOs().stream().map(this.cartItemMapper::fromRequestToEntity).toList();
            cart.setCartItems(cartItems);
        }

        // map user
        if(cartDTO.getUserId() != null) {
            User user = this.userRepository.findById(cartDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Người dùng với id " + cartDTO.getUserId() + " không tồn tại"));
            cart.setUser(user);
        }

        return cart ;
    }

    public CartResponse fromEntityToResponse (Cart cart){
        if(cart == null) return null ;
        if(fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = this.modelMapper.createTypeMap(Cart.class , CartResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(CartResponse :: setCartItemResponses);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }
        CartResponse cartResponse = fromEntityToResponseTypeMap.map(cart) ;

        // map cart items
        if(cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            List<CartItemResponse> cartItemResponses = cart.getCartItems()
                    .stream()
                    .map(this.cartItemMapper::fromEntityToResponse)
                    .toList();
            cartResponse.setCartItemResponses(cartItemResponses);
        }

        return cartResponse ;
    }
}
