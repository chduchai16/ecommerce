package com.example.shopapp.configurations;

import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserRepository userRepository ;

    @Bean
    public UserDetailsService userDetailsService() {
        return phoneNumber -> {
            try {
                Specification<com.example.shopapp.models.entities.User> spec = Specification.where(UserSpecification.hasPhoneNumberExact(phoneNumber));
                Optional<com.example.shopapp.models.entities.User> userOptional = userRepository.findOne(spec);
                if (userOptional.isEmpty()) {
                    throw new RuntimeException("Người dùng không tồn tại");
                }
                com.example.shopapp.models.entities.User user = userOptional.get();
                return User.withUsername(user.getPhoneNumber())
                        .password(user.getPassword())
                        .roles(user.getRole().getName()) // Role phải đúng định dạng ROLE_USER hoặc ROLE_ADMIN
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Lỗi xác thực: " + e.getMessage());
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
