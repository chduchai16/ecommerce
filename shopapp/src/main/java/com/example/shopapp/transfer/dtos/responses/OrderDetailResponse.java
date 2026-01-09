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

    @JsonProperty("product_name")
    private String productName ;

    @JsonProperty("product_id")
    private int productId ;

    private Float price ;

    @JsonProperty("product_image")
    private String productImage;

    private int quantity ;

    private Float total ;
}
