package com.example.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @JsonBackReference
    private Order order ;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product ;

    @Column(name = "quantity")
    private int quantity ;

    @Column(name = "total")
    private Float total ;

}
