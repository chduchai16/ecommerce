package com.example.shopapp.models.entities;

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
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "NTEXT")
    private String description;

    @Column(name = "price")
    private Float price;

    @Column(name = "original_price")
    private Float originalPrice;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "in_stock")
    private Boolean inStock;

    @Column(name = "tags", columnDefinition = "NVARCHAR(500)")
    private String tags; // Lưu dưới dạng JSON string hoặc comma-separated

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "brand", columnDefinition = "NVARCHAR(255)")
    private String brand;

    @Column(name = "color")
    private String color;

    @Column(name = "average_rating")
    private Float averageRating;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(name = "thumbnail", columnDefinition = "NVARCHAR(255)")
    private String thumbnail;

    private Long views;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonBackReference
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference
    private List<ProductSpecification> specifications = new ArrayList<>();
}
