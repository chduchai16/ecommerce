package com.example.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name = "name" , columnDefinition = "NVARCHAR(255)")
    private String name ;

    @Column(name = "phone_number" , length = 20)
    private String phoneNumber ;

    @Column(name = "email" , columnDefinition = "NVARCHAR(255)")
    private String email ;

    @Column(name = "address" , columnDefinition = "NVARCHAR(255)")
    private String address;

}
