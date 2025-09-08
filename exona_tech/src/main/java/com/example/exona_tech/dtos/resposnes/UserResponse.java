package com.example.exona_tech.dtos.resposnes;

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

    private RoleResponse role ;

}
