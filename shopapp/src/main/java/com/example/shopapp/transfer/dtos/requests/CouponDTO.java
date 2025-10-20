package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CouponDTO {

    @Nullable
    private Integer id ;

    @NotNull(message = "Mã giảm giá không được để trống")
    @NotBlank(message = "Mã giảm giá không được để trống")
    private String code ;

    @JsonProperty("discount_percent")
    private Float discountPercent ;

    @JsonProperty("discount_money")
    private Float discountMoney ;

    @JsonProperty("expiration_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-M-d")
    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate expirationDate;

    private String status ;
}
