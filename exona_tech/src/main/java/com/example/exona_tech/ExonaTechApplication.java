package com.example.exona_tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.domain.entities") // quét entity từ domain
@ComponentScan(basePackages = {"com.example.exona_tech", "com.example.domain"})
@EnableJpaRepositories(basePackages = "com.example.domain.repositories") // quét repository
public class ExonaTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExonaTechApplication.class, args);
	}

}
