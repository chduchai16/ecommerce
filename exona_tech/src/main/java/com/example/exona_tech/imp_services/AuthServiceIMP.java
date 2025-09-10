package com.example.exona_tech.imp_services;

import com.example.domain.configurations.JwtConfiguration;
import com.example.domain.entities.Role;
import com.example.domain.entities.User;
import com.example.domain.repositories.RoleRepository;
import com.example.domain.repositories.UserRepository;
import com.example.domain.services.IAuthService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
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
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new BadCredentialsException("Số điện thoại hoặc mật khẩu không đúng"));
        if (passwordEncoder.matches(password , user.getPassword())){
            return jwtConfiguration.generateToken(user) ;
        }
        else {
            throw new BadCredentialsException("Số điện thoại hoặc mật khẩu không đúng") ;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User signUp(User user) throws Exception {
        if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new DataIntegrityViolationException("Số điện thoại này đã tồn tại.");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword()) ;
        user.setPassword(encodedPassword);
        // mặc định là role customer
        Role role = roleRepository.findById(2).orElseThrow(()-> new EntityNotFoundException( "Vai trò này không tồn tại"));
        user.setRole(role);
        if(user.getUsername() == null) {
            String uuid = UUID.randomUUID().toString().substring(0,10);
            String name = "user_" + uuid ;
            user.setFullName(name);
        }
        return userRepository.save(user);
    }
}
