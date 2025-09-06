package com.example.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
@EqualsAndHashCode(callSuper = false)
public class Coupon extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name = "code", length = 255)
    private String code ;

    @Column(name = "discount_percent")
    private Float discountPercent;

    @Column(name = "discount_money")
    private Float discountMoney ;

    @Column(name = "expiration_date")
    private LocalDate expirationDate ;
}
