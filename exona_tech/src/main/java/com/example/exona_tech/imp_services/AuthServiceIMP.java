package com.example.exona_tech.imp_services;

import com.example.domain.configurations.JwtConfiguration;
import com.example.domain.entities.Role;
import com.example.domain.entities.User;
import com.example.domain.repositories.RoleRepository;
import com.example.domain.repositories.UserRepository;
import com.example.domain.services.IAuthService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceIMP implements IAuthService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtConfiguration jwtConfiguration ;
    private final RoleRepository roleRepository ;

    @Override
    public String signIn(String phoneNumber , String password) throws Exception {
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
    public User signUp(User user) throws Exception {
        if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword()) ;
            user.setPassword(encodedPassword);
            // mặc định là role customer
            Role role = roleRepository.findById(2).orElseThrow(()-> new EntityNotFoundException( "This role does not exist"));
            user.setRole(role);
            if(user.getUsername() == null) {
                String uuid = UUID.randomUUID().toString().substring(0,10);
                String name = "user_" + uuid ;
                user.setFullName(name);
            }
            return userRepository.save(user);
        }
        else {
            throw new Exception("This phone number exist.");
        }
    }
}
