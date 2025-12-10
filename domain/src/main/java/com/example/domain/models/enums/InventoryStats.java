package com.example.domain.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStats {

    @JsonProperty("total_products")
    private Integer totalProducts;        // Tổng số sản phẩm của seller

    @JsonProperty("total_stock")
    private Integer totalStock;           // Tổng số lượng tồn kho

    @JsonProperty("warning_count")
    private Integer warningCount;         // Số sản phẩm cảnh báo (tồn kho < 10)
}
