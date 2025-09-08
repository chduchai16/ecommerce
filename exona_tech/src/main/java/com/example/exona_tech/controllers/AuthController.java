package com.example.exona_tech.controllers;

import com.example.exona_tech.dtos.requests.UserDTO;
import com.example.exona_tech.dtos.requests.UserLoginDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.domain.entities.User;
import com.example.domain.services.IAuthService;
import com.example.exona_tech.mappers.UserMapper;
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
    private final UserMapper userMapper ;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
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
                System.out.println("Error sign in: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            String token = authService.signIn(userLoginDTO.getPhoneNumber() , userLoginDTO.getPassword());
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Sign in successfully.", token);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error sign in: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Sign in failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp (
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
                System.out.println("Error sign in: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userMapper.fromRequestToEntity(userDTO) ;
            authService.signUp(user);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Sign up user successfully.", userMapper.fromEntityToResponse(user));
            return ResponseEntity.ok(baseResponse);
        }
        catch (Exception e) {
            System.out.println("Error sign up: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Sign up failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
