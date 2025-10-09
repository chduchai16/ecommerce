package com.example.shopapp.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebSecurityFilter {

    @Value("${app.api-prefix}")
    private String apiPrefix;

    private final JwtAuthFilter jwtAuthFilter;
    private final DaoAuthenticationProvider authenticationProvider;
    private final String ADMIN = "ADMIN";
    private final String CUSTOMER = "CUSTOMER";
    private final String SELLER = "SELLER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // auth
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/auth/sign-in**").permitAll()
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/auth/sign-up**").permitAll()

                        // users - endpoint /me cho phép user đã đăng nhập truy cập
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/users/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/users/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/users**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/users/**").hasRole(ADMIN)

                        // categories (ai cũng xem được, chỉ admin quản lý)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/categories**").permitAll()
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/categories**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/categories**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/categories/**").hasRole(ADMIN)

                        // products
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/products**").permitAll()
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST , apiPrefix + "/products/batch**").permitAll()

                        // seller được quản lý sản phẩm của họ, admin thì toàn quyền
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/products**").hasAnyRole(SELLER, ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/products/many").hasAnyRole(SELLER, ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/products**").hasAnyRole(SELLER, ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/products/**").hasAnyRole(SELLER, ADMIN)

                        // orders
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/orders/user**").hasRole(CUSTOMER) // customer xem
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/orders**").hasRole(CUSTOMER) // customer
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/orders**").hasAnyRole(CUSTOMER, SELLER, ADMIN)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/orders/seller**").hasRole(SELLER) // seller xem

                        .requestMatchers(HttpMethod.GET, apiPrefix + "/orders**").hasRole(ADMIN) // admin xem tất cả

                        // carts (chỉ customer)
                        .requestMatchers(apiPrefix + "/carts/**").hasRole(CUSTOMER)
                        .requestMatchers(apiPrefix + "/carts**").hasRole(CUSTOMER)

                        // ratings
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/ratings**").hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/ratings**").hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/ratings/**").hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/ratings/product**").hasAnyRole(SELLER, ADMIN)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/ratings/user-product**").hasRole(CUSTOMER)

                        // coupons
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/coupons/apply**").hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/coupons**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/coupons**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/coupons**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/coupons/**").hasRole(ADMIN)

                        // roles (chỉ admin)
                        .requestMatchers(apiPrefix + "/roles/**").hasRole(ADMIN)

                        // media
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/media/image/**").permitAll()
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/media/uploads/products/**")
                        .hasAnyRole(SELLER, ADMIN)

                        // swagger
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()

                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Cho phép React frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Cho phép gửi cookie (nếu có)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho tất cả API
        return source;
    }
}
