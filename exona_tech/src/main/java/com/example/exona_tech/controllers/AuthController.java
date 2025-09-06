package com.example.exona_tech.controllers;

import com.example.domain.dtos.requests.UserDTO;
import com.example.domain.dtos.requests.UserLoginDTO;
import com.example.domain.dtos.resposnes.BaseResponse;
import com.example.domain.dtos.resposnes.UserResponse;
import com.example.domain.entities.User;
import com.example.domain.services.IAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api-prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Management", description = "Quản lý xác thực trong hệ thống")

public class AuthController {

    private final IAuthService authService ;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid UserLoginDTO userLoginDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Error login: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            String token = authService.login(userLoginDTO.getPhoneNumber() , userLoginDTO.getPassword());
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Login successfully.", token);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error login: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Login failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (
            @RequestBody @Valid UserDTO userDTO ,
            BindingResult result
    ){
        try {
            if (result.hasErrors()){
                StringBuilder errorsBuilder = new StringBuilder() ;
                for (FieldError fieldError : result.getFieldErrors()){
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Error register: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = authService.registerUser(userDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Register user successfully.", UserResponse.convertFromUser(user));
            return ResponseEntity.ok(baseResponse);

        }
        catch (Exception e) {
            System.out.println("Error register: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Register failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
