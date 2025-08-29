package com.example.domain.entities;

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
    private int id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user ;

    @Column(name = "total_price")
    private Float totalPrice ;

    @Column(name = "status" , length = 20)
    private String status ;

    @Column(name = "shipping_address" , length = 255)
    private String shippingAddress ;

    @Column(name = "payment_method", length = 255)
    private String paymentMethod ;

    @Column(name = "shipping_method" , length = 255)
    private String shippingMethod ;

    @Column(name = "fullname" , length =255)
    private String fullName ;

    @Column(name = "phone_number" , length=20)
    private String phoneNumber ;

    @Column(name = "email" , length=255)
    private String email ;

    @Column(name = "note")
    private String note ;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

}
