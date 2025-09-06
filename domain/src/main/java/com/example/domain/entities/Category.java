package com.example.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "categories")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name = "name" ,columnDefinition = "NVARCHAR(255)" , length = 255 , unique = true)
    private String name ;
}
