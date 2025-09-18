package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Cart;
import com.example.domain.models.entities.CartItem;
import com.example.domain.persistence.repositories.CartItemRepository;
import com.example.domain.persistence.repositories.CartRepository;
import com.example.domain.persistence.specifications.CartItemSpecification;
import com.example.domain.persistence.specifications.CartSpecification;
import com.example.domain.services.ICartItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceIMP implements ICartItemService {

    private final CartItemRepository cartItemRepository ;
    private final CartRepository cartRepository ;

    @Override
    public void deleteCartItem(int cartId, int cartItemId) throws Exception {

        Specification<Cart> cartSpec = Specification.where(CartSpecification.hasId(cartId));
        Optional<Cart> cartOpt = cartRepository.findOne(cartSpec);
        if(cartOpt.isEmpty()){
            throw new EntityNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId) ;
        }
        Cart existingCart = cartOpt.get();

        Specification<CartItem> cartItemSpec = Specification.where(CartItemSpecification.hasId(cartItemId))
                .and(CartItemSpecification.hasCartId(cartId));
        Optional<CartItem> cartItemOpt = cartItemRepository.findOne(cartItemSpec);
        if(cartItemOpt.isEmpty()){
            throw new EntityNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng với id: " + cartItemId) ;
        }
        CartItem existingCartItem = cartItemOpt.get();
        // Kiểm tra xem cartItem này có thuộc cart đang xử lý không
        if (!existingCart.getCartItems().contains(existingCartItem)) {
            throw new Exception("Giỏ hàng này không chứa sản phẩm được chỉ định.");
        }
        existingCart.getCartItems().remove(existingCartItem);
        cartRepository.save(existingCart); // JPA sẽ tự xóa orphan
    }

    @Override
    public void deleteCartItemsWithCartId(int cartId) throws Exception {
        Specification<Cart> cartSpec = Specification.where(CartSpecification.hasId(cartId));
        Optional<Cart> cartOpt = cartRepository.findOne(cartSpec);
        if(cartOpt.isEmpty()){
            throw new EntityNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId) ;
        }
        Cart existingCart = cartOpt.get();
        existingCart.getCartItems().clear();
        cartRepository.save(existingCart);
    }
}
