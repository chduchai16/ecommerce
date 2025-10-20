package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Nullable
    private Integer id;

    @JsonProperty("fullname")
    @NotNull(message = "Họ tên không được để trống")
    private String fullName;

    @JsonProperty("phone_number")
    @NotNull(message = "Số điện thoại không được để trống")
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    private String email;

    @NotNull(message = "Mật khẩu không được để trống")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @JsonProperty("retype_password")
    @NotNull(message = "Xác nhận mật khẩu không được để trống")
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String retypePassword;

    private String address;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Giới tính không được để trống")
    private String gender;

    @JsonProperty("role_id")
    @NotNull(message = "ID vai trò không được để trống")
    private int roleId;

    @JsonProperty("is_active")
    private int isActive;

    // Seller-specific fields (optional, only for seller registration)
    @JsonProperty("shop_name")
    private String shopName;

    @JsonProperty("shop_description")
    private String shopDescription;

    @JsonProperty("shop_logo")
    private String shopLogo;

    @JsonProperty("business_license")
    private String businessLicense;

    @JsonProperty("tax_code")
    private String taxCode;

}
