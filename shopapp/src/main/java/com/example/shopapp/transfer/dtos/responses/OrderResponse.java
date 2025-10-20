package com.example.shopapp.transfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int id;

    @JsonProperty("order_number")
    private String orderNumber;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("subtotal")
    private Float subtotal;

    @JsonProperty("shipping_fee")
    private Float shippingFee;

    @JsonProperty("discount")
    private Float discount;

    @JsonProperty("total_price")
    private Float totalPrice;

    @JsonProperty("final_amount")
    private Float finalAmount;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("delivered_date")
    private LocalDateTime deliveredDate;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("note")
    private String note;

    @JsonProperty("coupon_code")
    private String couponCode;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("order_date")
    private LocalDateTime createdAt;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetailResponses;
}