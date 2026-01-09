package com.example.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.shopapp.models.entities")
@EnableJpaRepositories(basePackages = "com.example.shopapp.repositories")
public class ShopappApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopappApplication.class, args);
    }
}
