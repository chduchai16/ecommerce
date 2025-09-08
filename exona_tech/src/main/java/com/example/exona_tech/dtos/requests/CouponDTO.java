package com.example.exona_tech.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @Nullable
    private Integer id ;

    @NotNull(message = "Code must not be null.")
    @NotBlank(message = "Code must not be blank.")
    private String code ;

    @JsonProperty("discount_percent")
    private Float discountPercent ;

    @JsonProperty("discount_money")
    private Float discountMoney ;

    @JsonProperty("expiration_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-M-d")
    @NotNull
    private LocalDate expirationDate;

    private String status ;
}
