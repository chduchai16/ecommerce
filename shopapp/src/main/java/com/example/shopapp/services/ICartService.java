package com.example.shopapp.services;

import com.example.shopapp.models.entities.Cart;
import com.example.shopapp.models.entities.CartItem;
import com.example.shopapp.models.entities.User;

public interface ICartService {
    Cart getCartByUserId(int userId) throws Exception;
    Cart updateCart(Cart cart) throws Exception;
}

