package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistoryResponse {

    private Integer id;

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("product_name")
    private String productName;

    private String action;

    @JsonProperty("quantity_before")
    private Integer quantityBefore;

    @JsonProperty("quantity_after")
    private Integer quantityAfter;

    @JsonProperty("quantity_change")
    private Integer quantityChange;

    @JsonProperty("performed_by")
    private String performedBy;

    private String reason;

    private String note;

    @JsonProperty("performed_at")
    private LocalDateTime performedAt;
}

