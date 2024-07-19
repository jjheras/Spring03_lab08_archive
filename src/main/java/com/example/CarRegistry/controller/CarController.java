package com.example.CarRegistry.controller;

import com.example.CarRegistry.controller.dto.CarBrandDTO;
import com.example.CarRegistry.controller.dto.CarDTO;
import com.example.CarRegistry.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class CarController {
    @Value("${car.defaultMake}")
    private String defaultMake;

    @Autowired
    private CarService carService;

    // Método para obtener todos los coches
    @GetMapping("/cars")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<CarDTO>> showCars() {
        log.info("Bienvenido al concesionario de: {}", defaultMake);
        log.info("Iniciando la solicitud para obtener todos los coches");
        try{
            List<CarDTO> cars = carService.getAllCars().get();
            return ResponseEntity.ok(cars);

        }catch (Exception e){
            log.error("Error al obtener todos los coches", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para agregar un nuevo coche
    @PostMapping("/addCar")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> addCar(@RequestBody CarDTO carDto) {
        try {
            carService.addCar(carDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al guardar el coche", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // Método para agregar una lista de coches
    @PostMapping("/addCars")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> addCars(@RequestBody List<CarDTO> carDtos) {
        try {
            carService.addCars(carDtos).get();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error al agregar el coche", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para eliminar un coche por ID
    @DeleteMapping("/delCar/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        try {
            carService.deleteCarById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error interno al eliminar el coche con id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // Método para actualizar información de un coche por ID
    @PutMapping("/putCar/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> putCar(@PathVariable Integer id, @RequestBody CarDTO carDto) {
        try {
            carService.updateCar(id, carDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error interno al actualizar el coche con id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para obtener un coche por ID
    @GetMapping("/getCar/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CarDTO> getCarId(@PathVariable Integer id) {
        try {
            CarDTO carDTO = carService.getCarById(id);
            return ResponseEntity.ok(carDTO);
        } catch (RuntimeException e) {
            log.error("Error interno al obtener el coche con id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para obtener todos los coches con sus marcas
    @GetMapping("/carsBrand")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<CarBrandDTO>> getAllCarsBrand() {
        try{
            List<CarBrandDTO> carBrandDTOS = carService.getAllCarBrand().get();
            return ResponseEntity.ok(carBrandDTOS);

        }catch (Exception e){
            log.error("Error al obtener todos los coches con sus marcas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para obtener un coche y su marca por ID
    @GetMapping("/carBrand/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CarBrandDTO> getCarBrand(@PathVariable Integer id) {
        try {
            CarBrandDTO carBrandDTO = carService.getCarBrandById(id);
            return ResponseEntity.ok(carBrandDTO);
        } catch (RuntimeException e) {
            log.error("Error interno al obtener el coche con marca por id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
