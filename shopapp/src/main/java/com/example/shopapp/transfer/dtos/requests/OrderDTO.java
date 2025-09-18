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
    @NotNull(message = "User id must not be null.")
    @Min(value = 1, message = "User id must not be < 1.")
    private Integer userId ;

    @JsonProperty("total_price")
    @NotNull(message = "Total price must not be null.")
    @Min(value = 0, message = "Total price must not be < 0.")
    private Float totalPrice ;

    @JsonProperty("status")
    private String status ;

    @JsonProperty("shipping_address")
    @NotNull(message = "Shipping address must not be null.")
    @Size(min = 5 , max = 255 , message = "Shipping address must be from 5 to 255 chars")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod ;

    @JsonProperty("shipping_method")
    private String shippingMethod ;

    @JsonProperty("customer_name")
    @NotNull(message = "customer name must not be null.")
    private String customerName ;

    @JsonProperty("phone_number")
    @NotNull(message = "Phone number must not be null.")
    private String phoneNumber ;

    @JsonProperty("email")
    private String email;

    private String note ;

    @JsonProperty("coupon_code")
    private String couponCode ;

    @JsonProperty("order_detail_ids")
    private List<Integer> orderDetailIds ;
}
