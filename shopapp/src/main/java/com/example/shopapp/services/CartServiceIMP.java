package com.example.shopapp.services;

import com.example.shopapp.models.entities.Cart;
import com.example.shopapp.models.entities.CartItem;
import com.example.shopapp.models.entities.Product;
import com.example.shopapp.models.entities.User;
import com.example.shopapp.repositories.CartRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.specifications.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceIMP implements ICartService {

    private final UserRepository userRepository ;
    private final CartRepository cartRepository ;
    private final ProductRepository productRepository ;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Cart getCartByUserId(int userId) throws Exception {
        Specification<User> spec = Specification.where(UserSpecification.hasId(userId));
        Optional<User> userOpt = userRepository.findOne(spec);
        if(userOpt.isEmpty()){
            throw new EntityNotFoundException("Không tìm thấy người dùng với id: " + userId) ;
        }
        if(userOpt.get().getCart() == null) {
            throw new Exception("Người dùng này chưa có sản phẩm nào trong giỏ hàng.") ;
        }
        return userOpt.get().getCart();
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart updateCart(Cart cart) throws Exception {

        // Kiểm tra cart
        Cart existingCart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Giỏ hàng này không tồn tại"));

        // Lấy danh sách CartItem hiện tại từ cart
        Map<Integer, CartItem> existingItemsMap = cart.getCartItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), item -> item));

        // lấy ra danh sách product id
        List<Integer> productIds = cart.getCartItems()
                .stream()
                .map(cartItem -> cartItem.getProduct().getId())
                .collect(Collectors.toList());

        // Lấy sản phẩm từ DB
        List<Product> products = productRepository.findAllById(productIds);
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        // Duyệt các DTO và xử lý cập nhật hoặc thêm mới
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = productMap.get(cartItem.getProduct().getId());
            if (product == null) {
                throw new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + cartItem.getProduct().getId());
            }

            CartItem existingItem = existingItemsMap.get(cartItem.getProduct().getId());
            if (existingItem != null) {
                // Nếu item đã tồn tại → cập nhật số lượng
                existingItem.setQuantity(cartItem.getQuantity());
                existingItemsMap.remove(cartItem.getProduct().getId()); // Đánh dấu là đã xử lý
            } else {
                // Nếu item chưa có → tạo mới và thêm vào cart
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(cartItem.getQuantity());
                cart.getCartItems().add(newItem);
            }
        }

        // Xóa các item không còn trong DTO
        for (CartItem itemToRemove : existingItemsMap.values()) {
            cart.getCartItems().remove(itemToRemove);
        }

        return cartRepository.save(cart);
    }

}
