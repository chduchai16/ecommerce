package com.example.shopapp.controllers;

import com.example.shopapp.models.entities.User;
import com.example.shopapp.services.IUserService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.UserDTO;
import com.example.shopapp.transfer.dtos.requests.UserPasswordDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // danh sách phân trang users
    @GetMapping()
    public ResponseEntity<?> filterUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone_number", required = false) String phoneNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit);
            Page<User> users = userService.filterUsers(name , email , phoneNumber , address , status , pageRequest);
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

            BaseResponse baseResponse = BaseResponse.buildResponse(20, "Lấy danh sách người dùng thành công.", pagedUsersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserWithId(@PathVariable("id") int userId) {
        try {
            User user = userService.getUserById(userId);
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = BaseResponse.buildResponse(200, "Lấy thông tin người dùng thành công.", userResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi lấy thông tin người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(404, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy thông tin người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserWithToken() {
        try {
            // Lấy thông tin xác thực từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication.getPrincipal() == null) {
                BaseResponse baseResponse = BaseResponse.buildResponse(401, "Không tìm thấy thông tin xác thực.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
            }

            // Lấy user từ principal (đã được set trong JwtAuthFilter)
            User user = (User) authentication.getPrincipal();
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = BaseResponse.buildResponse(200, "Lấy thông tin người dùng thành công.", userResponse);
            return ResponseEntity.ok(baseResponse);

        } catch (ClassCastException e) {
            System.out.println("Lỗi ép kiểu người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(401, "Token không hợp lệ.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy thông tin người dùng từ token: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                BaseResponse baseResponse = BaseResponse.buildResponse(400, "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userService.createUser(userMapper.fromRequestToEntity(userDTO));
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = BaseResponse.buildResponse(200, "Tạo người dùng thành công.",userResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Lỗi đăng ký: " + e);
             BaseResponse baseResponse = BaseResponse.buildResponse(409, e.getMessage());
             return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi đăng ký: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                BaseResponse baseResponse = BaseResponse.buildResponse(400, "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            User user = userService.updateUser(userMapper.fromRequestToEntity(userDTO)) ;
            UserResponse userResponse = userMapper.fromEntityToResponse(user);
            BaseResponse baseResponse = new BaseResponse(200 , "Cập nhật thành công." , userResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e) {
            System.out.println("Lỗi cập nhật người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(404, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (DataIntegrityViolationException e) {
            System.out.println("Lỗi cập nhật người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(409, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        }
        catch (Exception e) {
            System.out.println("Lỗi cập nhật người dùng: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody UserPasswordDTO userPasswordDTO
    ){
        try {
            userPasswordDTO.setId(user.getId());
            User userExists = userMapper.fromPasswordRequestToEntity(userPasswordDTO) ;
            userService.changeUserPassword(userExists);
            BaseResponse baseResponse = new BaseResponse(200 , "Đổi mật khẩu thành công." , null);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e) {
            System.out.println("Lỗi đổi mật khẩu: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(404, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Lỗi đổi mật khẩu: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(400, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);

        } catch (Exception e) {
            System.out.println("Lỗi đổi mật khẩu: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
