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
    @NotNull(message = "Full name must not be null.")
    private String fullName;

    @JsonProperty("phone_number")
    @NotNull(message = "Phone number must not be null.")
    @NotBlank(message = "Phone number must not be blank.")
    private String phoneNumber;

    private String email;

    @NotNull(message = "Password must not be null.")
    @NotBlank(message = "Password must not bt blank")
    private String password;

    @JsonProperty("retype_password")
    @NotNull(message = "Retype password must not be null.")
    @NotBlank(message = "Retype password must not be blank.")
    private String retypePassword;

    private String address;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender must not be null")
    private String gender;

    @JsonProperty("role_id")
    @NotNull(message = "Role id must not be null.")
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
