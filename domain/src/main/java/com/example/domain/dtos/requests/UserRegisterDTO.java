package com.example.domain.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @JsonProperty("phone_number")
    String phoneNumber ;
    String password ;
    @JsonProperty("retype_password")
    String retypePassword;
}
