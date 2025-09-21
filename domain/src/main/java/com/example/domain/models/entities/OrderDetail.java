package com.example.domain.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "price", nullable = false)
    private Float price; // Giá sản phẩm tại thời điểm đặt hàng

    @Column(name = "number_of_products", nullable = false)
    private Integer numberOfProducts; // Số lượng sản phẩm

    @Column(name = "total_money", nullable = false)
    private Float totalMoney; // Tổng tiền = price * numberOfProducts

}
