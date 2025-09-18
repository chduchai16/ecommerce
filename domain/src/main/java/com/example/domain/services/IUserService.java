package com.example.domain.services;

import com.example.domain.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    Page<User> filterUsers(String name, String email, String phoneNumber, String address, Integer status , Pageable pageable) throws Exception;
    User createUser(User user) throws Exception;
    User updateUser(User user) throws Exception;
    User getUserById(int userId) throws Exception;
    User getUserByPhoneNumber (String phoneNumber);
    void deleteUser(int userId);
}
