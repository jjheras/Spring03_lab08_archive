package com.example.CarRegistry.service;

import com.example.CarRegistry.controller.dto.BrandDTO;
import com.example.CarRegistry.controller.dto.CarDTO;
import com.example.CarRegistry.controller.mapper.BrandMapper;
import com.example.CarRegistry.repository.BrandRepository;
import com.example.CarRegistry.repository.entity.BrandEntity;
import com.example.CarRegistry.repository.entity.CarEntity;
import com.example.CarRegistry.repository.mapper.BrandEntityMapper;
import com.example.CarRegistry.service.impl.BrandServiceImpl;
import com.example.CarRegistry.service.impl.CarServiceImpl;
import com.example.CarRegistry.service.model.Brand;
import com.example.CarRegistry.service.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@ExtendWith(MockitoExtension.class)
public class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandEntityMapper brandEntityMapper;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private BrandEntity brandEntity;
    private Brand brand;
    private BrandDTO brandDTO;


    @BeforeEach
        //creo el estado de los objetos para utilizarlos en los distintos metodos
    void setUp() {
        //Crear objetos brand
        brandEntity = new BrandEntity(1, "testBrand", 1, "testCountry");
        brandDTO = new BrandDTO(1, "testBrand", 1, "testCountry");
        brand = new Brand(1, "testBrand", 1, "testCountry");
    }

    //prueba unitaria para agregar
    @Test
    void test_addBrand() {
        //Comportamiento de mappers
        when(brandMapper.brand(brandDTO)).thenReturn(brand);
        when(brandEntityMapper.brandEntity(brand)).thenReturn(brandEntity);
        //verificar que se guarda en el repositorio
        brandService.addBrand(brandDTO);
        //realizar las verificaciones
        verify(brandRepository).save(brandEntity);
        verify(brandMapper).brand(brandDTO);
        verify(brandEntityMapper).brandEntity(brand);

    }

    //prueba unitaria para agregar varias marcas
    @Test
    void test_addBrands() {
        //crear objetos simulados
        BrandDTO brandDTO1 = new BrandDTO(1, "testBrand1", 1, "testCountry1");
        BrandDTO brandDTO2 = new BrandDTO(2, "testBrand2", 2, "testCountry2");

        Brand brand1 = new Brand(1, "testBrand1", 1, "testCountry1");
        Brand brand2 = new Brand(2, "testBrand2", 2, "testCountry2");

        BrandEntity brandEntity1 = new BrandEntity(1, "testBrand1", 1, "testCountry1");
        BrandEntity brandEntity2 = new BrandEntity(2, "testBrand2", 2, "testCountry2");

        List<BrandDTO> brandDTOList = Arrays.asList(brandDTO1, brandDTO2);
        List<BrandEntity> brandEntityList = Arrays.asList(brandEntity1, brandEntity2);
        //configurar los mocks para devolver valores
        when(brandMapper.brand(brandDTO1)).thenReturn(brand1);
        when(brandMapper.brand(brandDTO2)).thenReturn(brand2);
        when(brandEntityMapper.brandEntity(brand1)).thenReturn(brandEntity1);
        when(brandEntityMapper.brandEntity(brand2)).thenReturn(brandEntity2);

        //llamar al metodo de prueba
        CompletableFuture<Void> result = brandService.addBrands(brandDTOList);

        //esperar a que la operación asincrona se complete
        result.join();

        //Hacer las verificaciones
        verify(brandRepository).saveAll(brandEntityList);
        verify(brandMapper).brand(brandDTO1);
        verify(brandMapper).brand(brandDTO2);
        verify(brandEntityMapper).brandEntity(brand1);
        verify(brandEntityMapper).brandEntity(brand2);

    }


    @Test
    void test_getBrandById() {
        //comportamientos los mocks
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));
        when(brandEntityMapper.brand(brandEntity)).thenReturn(brand);
        when(brandMapper.brandDto(brand)).thenReturn(brandDTO);
        //llamamos al metodo de prueba
        BrandDTO result = brandService.getBrandById(1);
        //verificamos el resultado
        assertEquals(result, brandDTO);
        //verificamos que las llamadas
        verify(brandEntityMapper).brand(brandEntity);
        verify(brandMapper).brandDto(brand);

    }

    @Test
    void test_getAllBrands() {
        BrandEntity brandEntity1 = new BrandEntity(1, "testBrand1", 1, "testCountry1");
        BrandEntity brandEntity2 = new BrandEntity(2, "testBrand2", 2, "testCountry2");

        Brand brand1 = new Brand(1, "testBrand1", 1, "testCountry1");
        Brand brand2 = new Brand(2, "testBrand2", 2, "testCountry2");

        BrandDTO brandDTO1 = new BrandDTO(1, "testBrand1", 1, "testCountry1");
        BrandDTO brandDTO2 = new BrandDTO(2, "testBrand2", 2, "testCountry2");

        //comprobamos los mocks
        when(brandRepository.findAll()).thenReturn(Arrays.asList(brandEntity1, brandEntity2));
        when(brandEntityMapper.brand(brandEntity1)).thenReturn(brand1);
        when(brandEntityMapper.brand(brandEntity2)).thenReturn(brand2);
        when(brandMapper.brandDto(brand1)).thenReturn(brandDTO1);
        when(brandMapper.brandDto(brand2)).thenReturn(brandDTO2);
        //llamamos al metodo de prueba
        CompletableFuture<List<BrandDTO>> result = brandService.getAllBrands();
        //verificamos el resultado
        List<BrandDTO> expected = Arrays.asList(brandDTO1, brandDTO2);
        //veririficar el resultado esperado
        assertEquals(result.join(), expected);
        //verificar las llamadas
        verify(brandRepository).findAll();
        verify(brandEntityMapper).brand(brandEntity1);
        verify(brandEntityMapper).brand(brandEntity2);
        verify(brandMapper).brandDto(brand1);
        verify(brandMapper).brandDto(brand2);
    }

    @Test
    void test_deleteBrandById() {
        //llamo al metodo de prueba
        brandService.deleteBrandById(1);

        //veirificar que fue llamado
        verify(brandRepository).deleteById(1);
    }

    @Test
    void test_updateBrand() {

        //configurar mock y crear conversion
        when(brandMapper.brand(brandDTO)).thenReturn(brand);
        when(brandEntityMapper.brandEntity(brand)).thenReturn(brandEntity);

        //Llamar al metodo
        brandService.updateBrand(1, brandDTO);

        //verificiar los procesos
        verify(brandRepository).save(brandEntity);
        verify(brandMapper).brand(brandDTO);
        verify(brandEntityMapper).brandEntity(brand);

    }


}
