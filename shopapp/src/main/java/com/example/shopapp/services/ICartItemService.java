package com.example.shopapp.services;

import com.example.shopapp.models.entities.CartItem;
import com.example.shopapp.models.entities.User;

public interface ICartItemService {
    void deleteCartItem(int cartId , int cartItemId) throws Exception;
    void deleteCartItemsWithCartId(int cartId) throws Exception ;
    CartItem addCartItemIntoCart(User user, CartItem cartItem) throws Exception;
}
