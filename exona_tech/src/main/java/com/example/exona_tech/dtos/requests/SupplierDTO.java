package com.example.exona_tech.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    @Nullable
    private Integer id ;

    @NotNull(message = "Supplier's name must not be null.")
    private String name ;

    @JsonProperty("phone_number")
    @NotNull(message = "Phone number must not be null.")
    private String phoneNumber ;

    private String email;

    private String address;

}
