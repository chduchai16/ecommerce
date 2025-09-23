package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private int id;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private String address;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;
    private String avatar;

    @JsonProperty("card_id")
    private int cartId;

    @JsonProperty("role_name")
    private String roleName ;

    // Seller-specific fields (for users with seller role)
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

    @JsonProperty("seller_rating")
    private Double sellerRating;

    @JsonProperty("total_sales")
    private Long totalSales;

    @JsonProperty("is_verified")
    private Boolean isVerified;

}
