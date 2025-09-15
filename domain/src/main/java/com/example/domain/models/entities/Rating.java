package com.example.domain.models.entities;

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
public class Rating extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product ;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user ;

    @Column(name = "rate")
    private int rate ;

    @Column(name = "comment" ,columnDefinition = "NVARCHAR(255)")
    private String comment ;
}
