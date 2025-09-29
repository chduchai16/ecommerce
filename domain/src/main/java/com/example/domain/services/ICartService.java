package com.example.domain.services;

import com.example.domain.models.entities.Cart;
import com.example.domain.models.entities.CartItem;
import com.example.domain.models.entities.User;

public interface ICartService {
    Cart getCartByUserId(int userId) throws Exception;
    Cart updateCart(Cart cart) throws Exception;
}

