package com.example.exona_tech.imp_services;

import com.example.domain.entities.Cart;
import com.example.domain.entities.CartItem;
import com.example.domain.repositories.CartItemRepository;
import com.example.domain.repositories.CartRepository;
import com.example.domain.services.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceIMP implements ICartItemService {

    private final CartItemRepository cartItemRepository ;
    private final CartRepository cartRepository ;

    @Override
    public void deleteCartItem(int cartId, int cartItemId) throws Exception {
        Cart existingCart = cartRepository.findById(cartId).orElseThrow(()-> new Exception("Cannot find this cart with id: " + cartId)) ;
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new Exception("Cannot find this cart.")) ;
        // Kiểm tra xem cartItem này có thuộc cart đang xử lý không
        if (!existingCart.getCartItems().contains(cartItem)) {
            throw new Exception("This cart does not contain the specified cart item.");
        }
        // Xóa cartItem khỏi danh sách của cart
        existingCart.getCartItems().remove(cartItem);
        // Nếu có quan hệ 2 chiều thì nên set null để tránh lỗi vòng tham chiếu
        cartItem.setCart(null);
        // Xóa cartItem khỏi database nếu cần
        cartItemRepository.delete(cartItem);
        // Lưu lại cart nếu cần (có cascade tự động xoá cartitem)
        cartRepository.save(existingCart);
    }

    @Override
    public void deleteCartItemsWithCartId(int cartId) throws Exception {
        if(cartRepository.findById(cartId).isEmpty()){
            throw new Exception("Cannot find this cart with id: " + cartId) ;
        }
        cartItemRepository.deleteCartItemsByCartId(cartId);
    }
}
