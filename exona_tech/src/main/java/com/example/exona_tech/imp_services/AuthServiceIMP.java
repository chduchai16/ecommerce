package com.example.exona_tech.imp_services;

import com.example.domain.configurations.JwtConfiguration;
import com.example.domain.dtos.requests.UserDTO;
import com.example.domain.entities.Role;
import com.example.domain.entities.User;
import com.example.domain.repositories.RoleRepository;
import com.example.domain.repositories.UserRepository;
import com.example.domain.services.IAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceIMP implements IAuthService {

    private final UserRepository userRepository ;
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtConfiguration jwtConfiguration ;

    @Override
    public String login(String phoneNumber , String password) throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()->new Exception("Wrong phone number or password"));
        if (passwordEncoder.matches(password , user.getPassword())){
            String token = jwtConfiguration.generateToken(user) ;
            return token ;
        }
        else {
            throw new Exception("Login failed.") ;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(UserDTO userDTO) throws Exception {
        if (!userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isEmpty()){
            throw new Exception("This phone number exist.");
        }
        else {
            User user = new User() ;
            String randomName = "user_" + UUID.randomUUID().toString().substring(0,12) ;
            user.setFullName(randomName);
            user.setPhoneNumber(userDTO.getPhoneNumber());
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                throw new Exception("Password does not match");
            }
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword()) ;
            user.setPassword(encodedPassword);
            user.setIsActive(true);
            user.setCart(null);
            Role role = roleRepository.findById(2).orElseThrow(()->new Exception("Role does not exist"));
            user.setRole(role);
            return userRepository.save(user);
        }
    }
}
