package com.example.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name ="roles")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @Column(name = "name" , length = 20)
    private String name ;

}
