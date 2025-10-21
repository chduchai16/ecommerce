package com.example.domain.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_number")
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "subtotal")
    private Float subtotal;

    @Column(name = "shipping_fee")
    private Float shippingFee;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "total_amount")
    private Float totalAmount;

    @Column(name = "final_amount")
    private Float finalAmount;

    // Order status: 0=pending, 1=confirmed, 2=shipping, 3=delivered, 4=cancelled
    @Column(name = "status")
    private Integer status;

    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;

    @Column(name = "shipping_address", columnDefinition = "NVARCHAR(255)", length = 255)
    private String shippingAddress;

    @Column(name = "payment_method", columnDefinition = "NVARCHAR(255)", length = 255)
    private String paymentMethod;

    @Column(name = "payment_status")
    private Integer paymentStatus;

    @Column(name = "shipping_method", columnDefinition = "NVARCHAR(255)", length = 255)
    private String shippingMethod;

    @Column(name = "customer_name", columnDefinition = "NVARCHAR(255)", length = 255)
    private String customerName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", columnDefinition = "NVARCHAR(255)", length = 255)
    private String email;

    @Column(name = "note", columnDefinition = "NVARCHAR(255)")
    private String note;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}