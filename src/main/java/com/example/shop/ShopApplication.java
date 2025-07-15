package com.example.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = "com.example.shop.entity")
public class ShopApplication {

    public static void main(String[] args) {

        System.out.println("Spring is running!");

        SpringApplication.run(ShopApplication.class, args);
    }

}
