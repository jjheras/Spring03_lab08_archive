package com.example.CarRegistry.service;

import com.example.CarRegistry.controller.dto.CarBrandDTO;
import com.example.CarRegistry.controller.dto.CarDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CarService {
    CompletableFuture<List<CarDTO>> getAllCars();

    CompletableFuture<List<CarBrandDTO>> getAllCarBrand();

    void addCar(CarDTO carDto);

    void deleteCarById(Integer id);

    CarDTO getCarById(Integer id);

    CarBrandDTO getCarBrandById(Integer id);

    void updateCar(Integer id, CarDTO carDto);

    CompletableFuture<Void> addCars(List<CarDTO> carDtos);
}
