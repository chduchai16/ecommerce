package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @Nullable
    private Integer id ;

    @JsonProperty("user_id")
    private Integer userId ;

    @JsonProperty("total_price")
    @NotNull(message = "Tổng giá không được để trống")
    @Min(value = 0, message = "Tổng giá không được nhỏ hơn 0")
    private Float totalPrice ;

    @JsonProperty("status")
    private String status ;

    @JsonProperty("shipping_address")
    @NotNull(message = "Địa chỉ giao hàng không được để trống")
    @Size(min = 5 , max = 255 , message = "Địa chỉ giao hàng phải từ 5 đến 255 ký tự")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod ;

    @JsonProperty("shipping_method")
    private String shippingMethod ;

    @JsonProperty("customer_name")
    @NotNull(message = "Tên khách hàng không được để trống")
    private String customerName ;

    @JsonProperty("phone_number")
    @NotNull(message = "Số điện thoại không được để trống")
    private String phoneNumber ;

    @JsonProperty("email")
    private String email;

    private String note ;

    @JsonProperty("coupon_code")
    private String couponCode ;

    @JsonProperty("order_details")
    @NotNull(message = "Đơn hàng phải có ít nhất một chi tiết")
    @Size(min = 1 , message = "Đơn hàng phải có ít nhất một chi tiết")
    private List<OrderDetailDTO> orderDetailDTOS ;
}
