package com.example.exona_tech.filters;

import com.example.domain.configurations.JwtConfiguration;
import com.example.domain.entities.User;
import com.example.domain.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtConfiguration jwtConfiguration;
    private final UserRepository userRepository;
    private List<Map<String, String>> permitAllEndpoints;

    @Value("${app.api-prefix}")
    private String apiPrefix ;

    // Danh sách các endpoint không yêu cầu xác thực (danh sách method + URL)
    @PostConstruct
    public void initPermitAllEndpoints() {
        this.permitAllEndpoints = List.of(
                Map.of("GET", apiPrefix + "/products"),
                Map.of("GET", apiPrefix + "/products/"),
                Map.of("GET", apiPrefix + "/categories"),
                Map.of("POST", apiPrefix + "/auth/login"),
                Map.of("POST", apiPrefix + "/auth/register")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod(); // Lấy phương thức HTTP (GET, POST, ...)

        // Kiểm tra nếu request nằm trong danh sách permitAll
        boolean isPermitAll = permitAllEndpoints.stream()
                .anyMatch(entry -> entry.get(requestMethod) != null && requestURI.startsWith(entry.get(requestMethod)));

        if (isPermitAll) {
            filterChain.doFilter(request, response);
            return;
        }

        // Xử lý xác thực JWT
        String authHeader = request.getHeader("Authorization");
        String phoneNumber = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            phoneNumber = jwtConfiguration.extractPhoneNumber(token);
        }

        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find this user"));
            if (jwtConfiguration.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
