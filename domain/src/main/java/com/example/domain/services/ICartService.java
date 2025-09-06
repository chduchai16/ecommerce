package com.example.domain.services;

import com.example.domain.entities.Cart;
import com.example.domain.entities.CartItem;

public interface ICartService {
    Cart getCartByUserId(int userId) throws Exception;
    Cart addCartItemIntoCart(int userId, CartItem cartItem) throws Exception;
    Cart updateCart(Cart cart) throws Exception;
}

