package com.example.domain.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Product product ;

    @Column(name = "image_name",columnDefinition = "NVARCHAR(255)" , length = 255)
    private String imageName ;
}
