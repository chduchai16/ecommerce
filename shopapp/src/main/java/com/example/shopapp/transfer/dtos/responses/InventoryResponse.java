package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private Integer id;

    private String name;

    private Float price;

    @JsonProperty("stock_quantity")
    private Integer stockQuantity;

    @JsonProperty("total_sold")
    private Long totalSold;

    private String brand;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("average_rating")
    private Float averageRating;

    private Integer status;

    @JsonProperty("warning_status")
    private String warningStatus; // "normal", "warning", "critical"
}
