package com.example.CarRegistry.controller;

import com.example.CarRegistry.controller.dto.BrandDTO;
import com.example.CarRegistry.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    // Método para obtener todas las marcas
    @GetMapping("/brand")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<BrandDTO>> showBrand() {
        log.info("Iniciando la solicitud para obtener todas las marcas");
        try{
            List<BrandDTO> brands = brandService.getAllBrands().get();
            return ResponseEntity.ok(brands);
        }catch (Exception e){
            log.error("Error al obtener todas las marcas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // Método para agregar una nueva marca
    @PostMapping("/addBrand")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> addBrand(@RequestBody BrandDTO brandDTO) {
        try {
            brandService.addBrand(brandDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al guardar la marca", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para agregar una lista de marcas
    @PostMapping("/addBrands")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> addBrands(@RequestBody List<BrandDTO> brandDtos) {
        try{
            brandService.addBrands(brandDtos).get();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error al agregar marcas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para eliminar una marca por ID
    @DeleteMapping("/delBrand/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        try {
            brandService.deleteBrandById(id);
            return ResponseEntity.ok().build();
        }  catch (NoSuchElementException e) {
            log.error("Marca no encontrada con id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (RuntimeException e) {
            log.error("Error interno al eliminar la marca con id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para actualizar información de una marca por ID
    @PutMapping("/putBrand/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> brandCar(@PathVariable Integer id, @RequestBody BrandDTO brandDTO) {
        try {
            brandService.updateBrand(id, brandDTO);
            return ResponseEntity.ok().build();
        }catch (NoSuchElementException e) {
            log.error("Marca no encontrada con id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            log.error("Error interno al actualizar la marca con id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para obtener una marca por ID
    @GetMapping("/getBrand/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BrandDTO> getBrandId(@PathVariable Integer id) {
        try {
            BrandDTO brandDTO = brandService.getBrandById(id);
            return ResponseEntity.ok(brandDTO);
        } catch (NoSuchElementException e) {
            log.error("Marca no encontrada con id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            log.error("Error interno al obtener la marca con id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
