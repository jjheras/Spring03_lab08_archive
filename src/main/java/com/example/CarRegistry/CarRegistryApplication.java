package com.example.CarRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.CarRegistry"})
public class CarRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRegistryApplication.class, args);
    }

}
