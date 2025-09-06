package com.example.domain.dtos.resposnes;

import com.example.domain.entities.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailResponse {

    private int id ;

    @JsonProperty("order_id")
    private int orderId ;

    @JsonProperty("product_response")
    private ProductResponse productResponse ;

    private int quantity ;

    private Float total ;

    public static OrderDetailResponse convertFromOrderDetail(OrderDetail orderDetail){
        OrderDetailResponse orderDetailResponse = OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productResponse(ProductResponse.convertFromProduct(orderDetail.getProduct()))
                .quantity(orderDetail.getQuantity())
                .total(orderDetail.getTotal())
                .build() ;
        return orderDetailResponse ;
    }
}
