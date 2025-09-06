package com.example.exona_tech.controllers;

import com.example.domain.dtos.requests.UserDTO;
import com.example.domain.dtos.requests.UserUpdateDTO;
import com.example.domain.dtos.resposnes.BaseResponse;
import com.example.domain.dtos.resposnes.PagedResponse;
import com.example.domain.dtos.resposnes.UserResponse;
import com.example.domain.entities.User;
import com.example.domain.pojos.PaginationInfo;
import com.example.domain.services.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/users")
@Tag(name = "User Management", description = "Quản lý người dùng trong hệ thống")
public class UserController {

    private final IUserService userService;

    // tìm kiếm user
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit);
            Page<User> users = userService.searchUsers(keyword, pageRequest);
            List<UserResponse> userResponses = users.getContent().stream()
                    .map(UserResponse::convertFromUser)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    users.getNumber() ,
                    users.getSize() ,
                    users.getTotalPages() ,
                    users.getTotalElements()
            ) ;

            PagedResponse pagedUsersResponse = new PagedResponse(userResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Search users successfully.", pagedUsersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error searching users: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Search users failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // danh sách phân trang users
    @GetMapping()
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit);
            Page<User> users = userService.getAllUsers(pageRequest);
            List<UserResponse> userResponses = users.getContent().stream()
                    .map(UserResponse::convertFromUser)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    users.getNumber() ,
                    users.getSize() ,
                    users.getTotalPages() ,
                    users.getTotalElements()
            ) ;

            PagedResponse pagedUsersResponse = new PagedResponse(userResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get users successfully.", pagedUsersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting users: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get users failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserWithId(@PathVariable("id") int userId) {
        try {
            User user = userService.getUserById(userId);
            UserResponse userResponse = UserResponse.convertFromUser(user);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get user successfully.", userResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting user: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get user failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/me/{phone_number}")
    public ResponseEntity<?> getUserWithPhoneNumber (@PathVariable("phone_number") String phoneNumber) {
        try{
            User user = userService.getUserByPhoneNumber(phoneNumber) ;
            UserResponse userResponse = UserResponse.convertFromUser(user) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Get user successfully." , userResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e ) {
            System.out.println("Error getting user: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get user failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(
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
                System.out.println("Error register: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userService.createUser(userDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Create user successfully.", UserResponse.convertFromUser(user));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error register: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Create user failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser (
            @PathVariable("id") int userId,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO ,
            BindingResult result
    ){
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Error update: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userService.updateUser(userId , userUpdateDTO) ;
            UserResponse userResponse = UserResponse.convertFromUser(user) ;
            BaseResponse baseResponse = new BaseResponse("200" , "Update successfully." , userResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Error update user: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Update user failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }


}
