package com.example.domain.services;

import com.example.domain.dtos.requests.UserDTO;
import com.example.domain.entities.User;

public interface IAuthService {
    String login(String phoneNumber , String password) throws Exception;
    User registerUser (UserDTO userDTO) throws Exception;
}
