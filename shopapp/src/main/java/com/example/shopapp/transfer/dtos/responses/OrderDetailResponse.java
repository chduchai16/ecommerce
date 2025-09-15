package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private int id ;

    @JsonProperty("order_id")
    private int orderId ;

    @JsonProperty("product_response")
    private ProductResponse productResponse ;

    private int quantity ;

    private Float total ;
}
