package com.example.shopapp.controllers;

import com.example.domain.models.entities.User;
import com.example.domain.services.IUserService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.UserDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.PagedResponse;
import com.example.shopapp.transfer.dtos.responses.UserResponse;
import com.example.shopapp.transfer.mappers.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final UserMapper userMapper ;

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
            List<UserResponse> userResponses = users.getContent()
                    .stream()
                    .map(userMapper :: fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    users.getNumber() ,
                    users.getSize() ,
                    users.getTotalPages() ,
                    users.getTotalElements()
            ) ;

            PagedResponse pagedUsersResponse = new PagedResponse(userResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tìm kiếm người dùng thành công.", pagedUsersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
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
            List<UserResponse> userResponses = users.getContent()
                    .stream()
                    .map(userMapper :: fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    users.getNumber() ,
                    users.getSize() ,
                    users.getTotalPages() ,
                    users.getTotalElements()
            ) ;

            PagedResponse pagedUsersResponse = new PagedResponse(userResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách người dùng thành công.", pagedUsersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserWithId(@PathVariable("id") int userId) {
        try {
            User user = userService.getUserById(userId);
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy thông tin người dùng thành công.", userResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi lấy thông tin người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy thông tin người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/me/{phone_number}")
    public ResponseEntity<?> getUserWithPhoneNumber (@PathVariable("phone_number") String phoneNumber) {
        try{
            User user = userService.getUserByPhoneNumber(phoneNumber) ;
            UserResponse userResponse = userMapper.fromEntityToResponse(user) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Lấy thông tin người dùng thành công." , userResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e ) {
            System.out.println("Lỗi lấy thông tin người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e ) {
            System.out.println("Lỗi lấy thông tin người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                System.out.println("Lỗi đăng ký: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userService.createUser(userMapper.fromRequestToEntity(userDTO));
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tạo người dùng thành công.",userResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Lỗi đăng ký: " + e);
             BaseResponse baseResponse = BaseResponse.buildResponse("409", e.getMessage());
             return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi đăng ký: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser (
            @RequestBody @Valid UserDTO userDTO ,
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
                System.out.println("Lỗi cập nhật: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userService.updateUser(userMapper.fromRequestToEntity(userDTO)) ;
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = new BaseResponse("200" , "Cập nhật thành công." , userResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e) {
            System.out.println("Lỗi cập nhật người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (DataIntegrityViolationException e) {
            System.out.println("Lỗi cập nhật người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("409", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        }
        catch (Exception e) {
            System.out.println("Lỗi cập nhật người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }


}
