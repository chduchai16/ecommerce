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
public class OrderDetailDTO {

    private Integer id ;

    @JsonProperty("order_id")
    @NotNull(message = "Order id must not be null.")
    private Integer orderId ;

    @JsonProperty("product_id")
    @NotNull(message = "Product id must not be null.")
    private Integer productId ;

    @JsonProperty("quantity")
    @NotNull(message = "Quantity must not be null.")
    @Min(value = 1 , message = "Quantity must not be < 1.")
    private int quantity ;

    @JsonProperty("total")
    @NotNull(message = "Total must not be null.")
    @Min(value = 0 , message = "Total must not be < 0.")
    private Float total;
}
