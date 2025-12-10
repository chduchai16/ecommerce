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
public class InventoryExportDTO {

    @JsonProperty("product_id")
    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;

    @JsonProperty("quantity")
    @NotNull(message = "Số lượng xuất không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @JsonProperty("reason")
    private String reason;
}

