package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustDTO {

    @JsonProperty("product_id")
    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;

    @JsonProperty("new_quantity")
    @NotNull(message = "Số lượng mới không được để trống")
    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer newQuantity;

    @JsonProperty("reason")
    private String reason;
}

