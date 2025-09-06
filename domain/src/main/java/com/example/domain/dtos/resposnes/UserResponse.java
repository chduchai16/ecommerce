package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Role;
import com.example.domain.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {

    private int id ;

    @JsonProperty("fullname")
    private String fullName ;

    @JsonProperty("phone_number")
    private String phoneNumber ;

    private String email ;

    private String address;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth ;

    private String gender ;
    private String avatar;

    @JsonProperty("card_id")
    private int cartId ;

    private Role role ;

    public static UserResponse convertFromUser(User user) {
        UserResponse userResponse = UserResponse
                .builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
        if(user.getCart() != null) {
            userResponse.setCartId(user.getCart().getId());
        }
        return userResponse ;
    }

}
