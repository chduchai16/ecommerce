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
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "name" , length = 255)
    private String name ;

    @Column(name = "phone_number" , length = 20)
    private String phoneNumber ;

    @Column(name = "email" , length = 255)
    private String email ;

    @Column(name = "address" , length = 255)
    private String address;

}
