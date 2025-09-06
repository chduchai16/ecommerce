package com.example.domain.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @JsonProperty("phone_number")
    @NotNull(message = "Phone number must not be null.")
    String phoneNumber ;

    @NotNull(message = "Password must not be null.")
    String password ;
}
