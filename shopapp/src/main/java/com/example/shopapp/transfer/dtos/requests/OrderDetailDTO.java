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
    private Integer orderId ;

    @JsonProperty("product_id")
    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId ;

    @JsonProperty("number_of_products")
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1 , message = "Số lượng phải lớn hơn 0")
    private int numberOfProducts ;

    private Float price ;

    @JsonProperty("total_money")
    @NotNull(message = "Tổng tiền không được để trống")
    @Min(value = 0 , message = "Tổng tiền không được nhỏ hơn 0")
    private Float totalMoney ;
}
