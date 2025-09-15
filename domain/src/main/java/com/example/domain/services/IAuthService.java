package com.example.domain.services;

import com.example.domain.models.entities.User;

public interface IAuthService {
    String signIn(String phoneNumber , String password) throws Exception;
    User signUp (User user) throws Exception;
}
