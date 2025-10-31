package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordDTO {

    private Integer id ;

    @JsonProperty("old_password")
    private String oldPassword ;

    @JsonProperty("new_password")
    private String newPassword ;

    @JsonProperty("confirm_new_password")
    private String confirmNewPassword ;
}
