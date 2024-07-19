package com.example.CarRegistry.service.impl;

import com.example.CarRegistry.controller.dto.BrandDTO;
import com.example.CarRegistry.controller.mapper.BrandMapper;
import com.example.CarRegistry.repository.BrandRepository;
import com.example.CarRegistry.repository.entity.BrandEntity;
import com.example.CarRegistry.repository.mapper.BrandEntityMapper;
import com.example.CarRegistry.service.BrandService;
import com.example.CarRegistry.service.model.Brand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandEntityMapper brandEntityMapper;
    @Autowired
    private BrandMapper brandMapper;
    // Método para obtener todas las marcas
    @Override
    //Utilizamos en este metodo asincronia por que accede a la BBDD para obtener gran volumen de informacion.
    @Async("taskExecutor")
    public CompletableFuture<List<BrandDTO>> getAllBrands() {
        long startTime = System.currentTimeMillis();
        //es la manera de ejecutar una tarea asincronica, pero que devuelve información.
        return CompletableFuture.supplyAsync(()->{

                List<BrandEntity> brands = brandRepository.findAll();
                if(brands.isEmpty()){
                    log.warn("No se encuentran marcas en BBDD");
                    throw new RuntimeException("No se encuentran marcas en BBDD");
                }
                List<BrandDTO> brandDTOS = brands.stream()
                        .map(brandEntityMapper::brand).map(brandMapper::brandDto)
                        .collect(Collectors.toList());
                long endTime = System.currentTimeMillis();
                //Mostramos el mensaje de finalización con el tiempo que ha tardado en realizar la operación.
                log.info("Finalizando getAllBrands en {} ms", (endTime - startTime));
                return brandDTOS;
        });

    }
    // Método para agregar una nueva marca
    @Override
    public void addBrand(BrandDTO brandDto) {
                BrandEntity brandEntity = brandEntityMapper.brandEntity(brandMapper.brand(brandDto));
                brandRepository.save(brandEntity);
    }
    @Override
    //Utilizamos en este metodo asincronia por que accede a la BBDD para poder guardadr gran cantidad de información.
    @Async("taskExecutor")
    public CompletableFuture<Void> addBrands(List<BrandDTO> brandDtos) {
        long startTime = System.currentTimeMillis();
        //es la manera de ejecutar una tarea asincronica sin que devuelva nada
        return CompletableFuture.runAsync(()-> {
                List<BrandEntity> brandEntities = brandDtos.stream()
                        .map(brandMapper::brand)
                        .map(brandEntityMapper::brandEntity)
                        .collect(Collectors.toList());
                brandRepository.saveAll(brandEntities);
                long endTime = System.currentTimeMillis();
                //Mostramos el mensaje de finalización con el tiempo que ha tardado en realizar la operación.
                log.info("Finalizando addBrands en {} ms", (endTime - startTime));
        });

    }
    // Método para eliminar una marca por ID
    @Override
    public void deleteBrandById(Integer id) {
                brandRepository.deleteById(id);
    }
    // Método para obtener una marca por ID
    @Override
    public BrandDTO getBrandById(Integer id) {
        //prueba de test
        Optional<BrandEntity> brandEntity = brandRepository.findById(id);

        return  brandRepository.findById(id).map(brandEntityMapper::brand).map(brandMapper::brandDto)
                .orElseThrow(()-> new NoSuchElementException("El brand con el ID " + id + " no existe."));
    }
    // Método para actualizar una marca por ID
    @Override
    public void updateBrand(Integer id, BrandDTO brandDto) {
        Brand brand = brandMapper.brand(brandDto);
        BrandEntity brandEntity = brandEntityMapper.brandEntity(brand);
        brandEntity.setId(id);
        brandRepository.save(brandEntity);
    }
}
