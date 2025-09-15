package com.example.domain.services;

import com.example.domain.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    User createUser(User user) throws Exception;
    void deleteUser(int userId) throws Exception;
    User updateUser(User user) throws Exception;
    User getUserById(int userId) throws Exception;
    Page<User> searchUsers(String keyword ,Pageable pageable);
    Page<User> getAllUsers(Pageable pageable);
    User getUserByPhoneNumber (String phoneNumber) throws Exception;
}
