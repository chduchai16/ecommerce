package com.example.exona_tech.controllers;

import com.example.domain.dtos.requests.RoleDTO;
import com.example.domain.dtos.resposnes.BaseResponse;
import com.example.domain.dtos.resposnes.RoleResponse;
import com.example.domain.entities.Role;
import com.example.domain.services.IRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping()
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleResponse> roleResponses = roles.stream().map(RoleResponse::convertFromRole).toList();
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get roles successfully.", roleResponses);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting roles: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get roles failed.");
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
                System.out.println("Error creating role: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid role data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Role role = roleService.createRole(roleDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Create role successfully.", RoleResponse.convertFromRole(role));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error creating role: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Create role failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
