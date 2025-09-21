package com.example.shopapp.transfer.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {

    private Integer id;

    @NotBlank(message = "Seller name must not be blank")
    private String name;

    private Double rating;
}