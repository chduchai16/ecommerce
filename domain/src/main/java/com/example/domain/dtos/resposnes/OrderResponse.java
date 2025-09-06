package com.example.domain.dtos.resposnes;

import com.example.domain.entities.Order;
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

    @JsonProperty("fullname")
    private String fullName ;

    @JsonProperty("phone_number")
    private String phoneNumber ;

    @JsonProperty("email")
    private String email;

    @JsonProperty("note")
    private String note ;

    @JsonProperty("order_detail_responses")
    private List<OrderDetailResponse> orderDetailResponses ;

    public static OrderResponse convertFromOrder(Order order){

        List<OrderDetailResponse> orderDetailResponsesConverted = order.getOrderDetails()
                .stream()
                .map(OrderDetailResponse::convertFromOrderDetail)
                .toList() ;


        OrderResponse orderResponse = OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .shippingMethod(order.getShippingMethod())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .note(order.getNote())
                .orderDetailResponses(orderDetailResponsesConverted )
                .build();
        return orderResponse ;
    }

}
