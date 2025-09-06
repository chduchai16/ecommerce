package com.example.domain.services;

public interface ICartItemService {
    void deleteCartItem(int cartId , int cartItemId) throws Exception;
    void deleteCartItemsWithCartId(int cartId) throws Exception ;
}
