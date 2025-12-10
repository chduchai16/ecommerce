package com.example.shopapp.services;

import com.example.shopapp.models.entities.User;

public interface IAuthService {
    String signIn(String phoneNumber , String password , Integer role ,  Boolean remember) throws Exception;
    User signUp (User user) throws Exception;
}
