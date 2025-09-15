package com.example.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.exona_tech", "com.example.domain", "com.example.shopapp"})
@EntityScan(basePackages = "com.example.domain.models.entities")
@EnableJpaRepositories(basePackages = "com.example.domain.persistence.repositories")
public class ShopappApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopappApplication.class, args);
    }
}
