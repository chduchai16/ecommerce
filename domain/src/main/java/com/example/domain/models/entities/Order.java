package com.example.domain.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user ;

    @Column(name = "total_amount")
    private Float totalAmount ;

    @Column(name = "status" ,columnDefinition = "NVARCHAR(255)", length = 20)
    private String status ;

    @Column(name = "shipping_address" ,columnDefinition = "NVARCHAR(255)", length = 255)
    private String shippingAddress ;

    @Column(name = "payment_method",columnDefinition = "NVARCHAR(255)", length = 255)
    private String paymentMethod ;

    @Column(name = "shipping_method",columnDefinition = "NVARCHAR(255)" , length = 255)
    private String shippingMethod ;

    @Column(name = "customerName",columnDefinition = "NVARCHAR(255)" , length =255)
    private String customerName ;

    @Column(name = "phone_number" , length=20)
    private String phoneNumber ;

    @Column(name = "email",columnDefinition = "NVARCHAR(255)" , length=255)
    private String email ;

    @Column(name = "note" ,columnDefinition = "NVARCHAR(255)")
    private String note ;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

}
