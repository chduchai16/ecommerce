package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int id ;

    @JsonProperty("user_id")
    private int userId ;

    @JsonProperty("total_price")
    private Float totalPrice ;

    @JsonProperty("status")
    private String status ;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod ;

    @JsonProperty("shipping_method")
    private String shippingMethod ;

    @JsonProperty("customer_name")
    private String customerName ;

    @JsonProperty("phone_number")
    private String phoneNumber ;

    @JsonProperty("email")
    private String email;

    @JsonProperty("note")
    private String note ;

    @JsonProperty("coupon_code")
    private String couponCode ;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetailResponses ;

}
