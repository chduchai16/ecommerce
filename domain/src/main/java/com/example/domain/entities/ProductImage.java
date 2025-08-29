package com.example.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Product product ;

    @Column(name = "image_name" , length = 255)
    private String imageName ;
}
