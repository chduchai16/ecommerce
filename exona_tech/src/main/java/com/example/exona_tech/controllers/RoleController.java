package com.example.exona_tech.controllers;

import com.example.exona_tech.dtos.requests.RoleDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.exona_tech.dtos.resposnes.RoleResponse;
import com.example.domain.entities.Role;
import com.example.domain.services.IRoleService;
import com.example.exona_tech.mappers.RoleMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/roles")
@Tag(name = "Role Management", description = "Quản lý vai trò trong hệ thống")

public class RoleController {

    private final IRoleService roleService;
    private final RoleMapper roleMapper ;

    @GetMapping()
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleResponse> roleResponses = roles
                    .stream()
                    .map(roleMapper :: fromEntityToResponse)
                    .toList();
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách vai trò thành công.", roleResponses);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách vai trò: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createRole(
            @RequestBody @Valid RoleDTO roleDTO,
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
                System.out.println("Lỗi tạo vai trò: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Role role = roleService.createRole(roleMapper.fromRequestToEntity(roleDTO));
            RoleResponse roleResponse = roleMapper.fromEntityToResponse(role) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tạo vai trò thành công.",roleResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Lỗi tạo vai trò: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("409", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi tạo vai trò: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
