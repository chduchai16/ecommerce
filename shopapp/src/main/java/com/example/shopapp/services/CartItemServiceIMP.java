package com.example.shopapp.services;

import com.example.shopapp.models.entities.Cart;
import com.example.shopapp.models.entities.CartItem;
import com.example.shopapp.models.entities.Product;
import com.example.shopapp.models.entities.User;
import com.example.shopapp.repositories.CartItemRepository;
import com.example.shopapp.repositories.CartRepository;
import com.example.shopapp.specifications.CartItemSpecification;
import com.example.shopapp.specifications.CartSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItem addCartItemIntoCart(User user, CartItem cartItem) {
        Product product = cartItem.getProduct();

        // lấy cart từ user
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
            user.setCart(cart); // gắn lại vào user nếu là quan hệ 1-1
            cartRepository.save(cart); // lưu mới cart
        }

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        CartItem targetItem;

        // kiểm tra sản phẩm đã có chưa
        Optional<CartItem> existingItemOpt = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // nếu có rồi thì cộng dồn số lượng
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            targetItem = existingItem;
        } else {
            // chưa có thì tạo mới
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cartItem.getQuantity());
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
            targetItem = newItem;
        }
        return cartItemRepository.save(targetItem);
    }
}
