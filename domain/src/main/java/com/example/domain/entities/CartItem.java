package com.example.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @JsonManagedReference
    private Product product ;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    @ToString.Exclude
    private Cart cart ;

    private int quantity ;
}
