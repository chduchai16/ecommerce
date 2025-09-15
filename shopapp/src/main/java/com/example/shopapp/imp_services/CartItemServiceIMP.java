package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Cart;
import com.example.domain.models.entities.CartItem;
import com.example.domain.persistence.repositories.CartItemRepository;
import com.example.domain.persistence.repositories.CartRepository;
import com.example.domain.services.ICartItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceIMP implements ICartItemService {

    private final CartItemRepository cartItemRepository ;
    private final CartRepository cartRepository ;

    @Override
    public void deleteCartItem(int cartId, int cartItemId) throws Exception {
        Cart existingCart = cartRepository.findById(cartId).orElseThrow(()-> new EntityNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId)) ;
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng.")) ;
        // Kiểm tra xem cartItem này có thuộc cart đang xử lý không
        if (!existingCart.getCartItems().contains(cartItem)) {
            throw new Exception("Giỏ hàng này không chứa sản phẩm được chỉ định.");
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
            throw new EntityNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId) ;
        }
        cartItemRepository.deleteCartItemsByCartId(cartId);
    }
}
