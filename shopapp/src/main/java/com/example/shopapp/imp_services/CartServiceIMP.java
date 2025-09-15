package com.example.shopapp.imp_services;


import com.example.domain.models.entities.Cart;
import com.example.domain.models.entities.CartItem;
import com.example.domain.models.entities.Product;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.CartItemRepository;
import com.example.domain.persistence.repositories.CartRepository;
import com.example.domain.persistence.repositories.ProductRepository;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.domain.services.ICartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final CartItemRepository cartItemRepository ;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Cart getCartByUserId(int userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy người dùng này.")) ;
        if(user.getCart() == null){
            throw new Exception("Người dùng này chưa có sản phẩm nào trong giỏ hàng.") ;
        }
        return user.getCart() ;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart addCartItemIntoCart(int userId, CartItem cartItem) throws Exception {
        // kiểm tra user và product có tô tại không
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng này."));
        Product product = cartItem.getProduct();

        // nếu đã có cart thì lấy còn khong thì tạo mới
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return newCart;
                });

        // lọc items có product như trên
        Optional<CartItem> existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        // đã có thì thêm số lượng, còn không thì tạo mới rồi thêm vào giỏ hàng
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cartItem.getQuantity());
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart updateCart(Cart cart) throws Exception {
        // Kiểm tra user

        User user = new User();
        if(cart.getUser() != null) {
            user = cart.getUser();
        }

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
