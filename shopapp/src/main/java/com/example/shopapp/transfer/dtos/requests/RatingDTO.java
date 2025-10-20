package com.example.shopapp.transfer.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    @Nullable
    private Integer id ;

    @NotNull(message = "ID sản phẩm không được để trống")
    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("user_id")
    @NotNull(message = "ID người dùng không được để trống")
    private Integer userId;

    @NotNull(message = "Điểm đánh giá không được để trống")
    @Max(value = 5,message = "Điểm đánh giá phải nhỏ hơn 6")
    @Min(value = 1,message = "Điểm đánh giá phải lớn hơn 0")
    private int rate ;

    private String comment ;
}
