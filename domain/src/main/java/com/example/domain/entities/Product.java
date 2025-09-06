package com.example.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name = "name" ,columnDefinition = "NVARCHAR(255)", length =255)
    private String name ;

    @Column(name = "description" ,columnDefinition = "NVARCHAR(255)", length =255)
    private String description ;

    @Column(name = "price")
    private Float price ;

    @Column(name = "stock_quantity")
    private int stockQuantity ;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category ;

    @Column(name = "brand",columnDefinition = "NVARCHAR(255)")
    private String brand;

    @Column(name = "color")
    private String color ;

    @Column(name = "average_rating" , columnDefinition = "FLOAT DEFAULT 0")
    private Float averageRating;

    @ManyToOne
    @JoinColumn(name="supplier_id" , nullable = true)
    private Supplier supplier ;

    @Column(name = "thumbnail",columnDefinition = "NVARCHAR(255)")
    private String thumbnail ;

    private Long views ;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonBackReference
    private List<CartItem> cartItems = new ArrayList<>() ;

    @OneToMany(mappedBy = "product" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference
    private List<ProductImage> productImages = new ArrayList<>();
}
