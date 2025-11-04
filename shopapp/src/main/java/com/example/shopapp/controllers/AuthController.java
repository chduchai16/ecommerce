package com.example.shopapp.controllers;

import com.example.domain.models.entities.User;
import com.example.domain.services.IAuthService;
import com.example.shopapp.transfer.dtos.requests.UserDTO;
import com.example.shopapp.transfer.dtos.requests.UserLoginDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.mappers.UserMapper;
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

@RestController()
@RequestMapping("${app.api-prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Management", description = "Quản lý xác thực trong hệ thống")
public class AuthController {

    private final IAuthService authService;
    private final UserMapper userMapper;

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
                System.out.println("Lỗi đăng nhập: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse(400, "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            String token = authService.signIn(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword() , userLoginDTO.getRole() , userLoginDTO.getRemember());
            BaseResponse baseResponse = BaseResponse.buildResponse(200, "Đăng nhập thành công.", token);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi đăng nhập: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Đăng nhập thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @RequestBody @Valid UserDTO userDTO,
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
                System.out.println("Lỗi đăng ký: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse(400, "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userMapper.fromRequestToEntity(userDTO);
            authService.signUp(user);
            BaseResponse baseResponse = BaseResponse.buildResponse(200, "Đăng ký tài khoản thành công.", userMapper.fromEntityToResponse(user));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi đăng ký: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Đăng ký thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
