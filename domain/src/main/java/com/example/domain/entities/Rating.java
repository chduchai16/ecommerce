package com.example.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product ;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user ;

    @Column(name = "rate")
    private int rate ;

    @Column(name = "comment" , length = 100)
    private String comment ;
}
