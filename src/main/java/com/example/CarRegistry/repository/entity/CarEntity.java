package com.example.CarRegistry.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer brandid;
    private String model;
    private Integer year;
    private Integer numberofdoors;
    private Boolean isconvertible;
    private Integer mileage;
    private Double price;
    private String description;
    private String colour;
    private String fueltype;
}
