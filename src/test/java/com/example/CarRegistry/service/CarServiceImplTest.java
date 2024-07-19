package com.example.CarRegistry.service;

import com.example.CarRegistry.controller.dto.BrandDTO;
import com.example.CarRegistry.controller.dto.CarBrandDTO;
import com.example.CarRegistry.controller.dto.CarDTO;
import com.example.CarRegistry.controller.mapper.BrandMapper;
import com.example.CarRegistry.repository.BrandRepository;
import com.example.CarRegistry.repository.CarRepository;
import com.example.CarRegistry.repository.entity.BrandEntity;
import com.example.CarRegistry.repository.entity.CarEntity;
import com.example.CarRegistry.repository.mapper.CarEntityMapper;
import com.example.CarRegistry.service.impl.CarServiceImpl;
import com.example.CarRegistry.service.model.Brand;
import com.example.CarRegistry.service.model.Car;
import com.example.CarRegistry.controller.mapper.CarMapper;
import com.example.CarRegistry.repository.mapper.BrandEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarEntityMapper carEntityMapper;

    @Mock
    private BrandEntityMapper brandEntityMapper;

    @Mock
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private CarServiceImpl carService;

    private CarEntity carEntity;
    private Car car;
    private CarDTO carDTO;
    private BrandEntity brandEntity;
    private Brand brand;
    private BrandDTO brandDTO;


    @BeforeEach
        //creo el estado de los objetos para utilizarlos en los distintos metodos
    void setUp() {
        //Crear objetos Car
        carEntity = new CarEntity(1, 1, "testModel", 2020, 4, false, 10000, 2000.0, "testDescription", "testColour", "testFuel");
        car = new Car(1, 1, "testModel", 2020, 4, false, 10000, 2000.0, "testDescription", "testColour", "testFuel");
        carDTO = new CarDTO(1, 1, "testModel", 2020, 4, false, 10000, 2000.0, "testDescription", "testColour", "testFuel");

        //Crear objetos Brand
        brandDTO = new BrandDTO(1, "testBrand", 1, "testCountry");
        brandEntity = new BrandEntity(1, "testBrand", 1, "testCountry");
        brand = new Brand(1, "testBrand", 1, "testCountry");
    }

    @Test
    void test_getAllCars() throws Exception {
        //Comportamiento de repositorio y mapeado
        when(carRepository.findAll()).thenReturn(Arrays.asList(carEntity));
        when(carEntityMapper.car(carEntity)).thenReturn(car);
        when(carMapper.carDto(car)).thenReturn(carDTO);

        //llamar al metodo
        CompletableFuture<List<CarDTO>> result = carService.getAllCars();

        //verificar resultado
        List<CarDTO> expected = Arrays.asList(carDTO);
        assertEquals(expected, result.get());

        //verificaciones
        verify(carRepository).findAll();
        verify(carEntityMapper).car(carEntity);
        verify(carMapper).carDto(car);

    }

    @Test
    void test_getAllCarBrand() throws Exception {

        //Comportamiento de repositorio y mapeado
        when(carRepository.findAll()).thenReturn(Arrays.asList(carEntity));
        when(brandRepository.findById(carEntity.getBrandid())).thenReturn(Optional.of(brandEntity));
        when(brandEntityMapper.brand(brandEntity)).thenReturn(brand);
        when(brandMapper.brandDto(brand)).thenReturn(brandDTO);
        when(carEntityMapper.car(carEntity)).thenReturn(car);
        when(carMapper.carDto(car)).thenReturn(carDTO);
        //llamada al metodo
        CompletableFuture<List<CarBrandDTO>> result = carService.getAllCarBrand();

        //verificar resultado
        CarBrandDTO expectedCarBrandDTO = CarBrandDTO.builder()
                .id(carDTO.getId())
                .brand(brandDTO)
                .model(carDTO.getModel())
                .year(carDTO.getYear())
                .numberofdoors(carDTO.getNumberofdoors())
                .isconvertible(carDTO.getIsconvertible())
                .mileage(carDTO.getMileage())
                .price(carDTO.getPrice())
                .description(carDTO.getDescription())
                .colour(carDTO.getColour())
                .fueltype(carDTO.getFueltype())
                .build();
        List<CarBrandDTO> expected = Arrays.asList(expectedCarBrandDTO);
        assertEquals(expected, result.get());

        //verificaciones
        verify(carRepository).findAll();
        verify(brandRepository).findById(carEntity.getBrandid());
        verify(brandEntityMapper).brand(brandEntity);
        verify(carEntityMapper).car(carEntity);
        verify(carMapper).carDto(car);
    }

    @Test
    void test_addCar() throws Exception {
        //comportamiento del mapeado
        when(carMapper.car(carDTO)).thenReturn(car);
        when(carEntityMapper.carEntity(car)).thenReturn(carEntity);

        //llamar al metodo
        carService.addCar(carDTO);

        //Verificación llamada a repositorio
        verify(carRepository).save(carEntity);

    }

    @Test
    void test_addCars() throws Exception {
        //comportamiento mapeado
        List<CarDTO> carDTOList = Arrays.asList(carDTO);
        List<CarEntity> carEntityList = Arrays.asList(carEntity);

        when(carMapper.car(carDTO)).thenReturn(car);
        when(carEntityMapper.carEntity(car)).thenReturn(carEntity);

        //llamar al metodo
        CompletableFuture<Void> result = carService.addCars(carDTOList);

        //esperar que la opoeración asincronica se complete
        result.join();

        //Verificación llamada a repositorio
        verify(carRepository).saveAll(carEntityList);
        verify(carMapper).car(carDTO);
        verify(carEntityMapper).carEntity(car);

        //Comprobar si se completo sin excepciones
        result.get();
    }

    @Test
    void test_deleteCarById() {
        //llamar al metodo
        carService.deleteCarById(1);
        //verificar la llamada
        verify(carRepository).deleteById(1);
    }

    @Test
    void test_getCarById() {
        //comportamiento repositorio y mapeo
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(carEntityMapper.car(carEntity)).thenReturn(car);
        when(carMapper.carDto(car)).thenReturn(carDTO);

        //llamar al metodo
        CarDTO result = carService.getCarById(1);

        //verificar resultado
        assertEquals(carDTO, result);

        //verificar interacciones
        verify(carRepository).findById(1);
        verify(carEntityMapper).car(carEntity);
        verify(carMapper).carDto(car);

    }

    @Test
    void test_getCarBrandById() {
        //comportamiento del respositorio y mappers
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(brandRepository.findById(carEntity.getBrandid())).thenReturn(Optional.of(brandEntity));
        when(brandEntityMapper.brand(brandEntity)).thenReturn(brand);
        when(brandMapper.brandDto(brand)).thenReturn(brandDTO);
        when(carEntityMapper.car(carEntity)).thenReturn(car);
        when(carMapper.carDto(car)).thenReturn(carDTO);

        //llamar al metodo
        CarBrandDTO result = carService.getCarBrandById(1);

        //verificar resultado
        CarBrandDTO expectedCarBrandDTO = CarBrandDTO.builder()
                .id(carDTO.getId())
                .brand(brandDTO)
                .model(carDTO.getModel())
                .year(carDTO.getYear())
                .numberofdoors(carDTO.getNumberofdoors())
                .isconvertible(carDTO.getIsconvertible())
                .mileage(carDTO.getMileage())
                .price(carDTO.getPrice())
                .description(carDTO.getDescription())
                .colour(carDTO.getColour())
                .fueltype(carDTO.getFueltype())
                .build();
        assertEquals(expectedCarBrandDTO, result);

        //verificar interacciones
        verify(carRepository).findById(1);
        verify(brandRepository).findById(carEntity.getBrandid());
        verify(brandEntityMapper).brand(brandEntity);
        verify(brandMapper).brandDto(brand);
        verify(carEntityMapper).car(carEntity);
        verify(carMapper).carDto(car);
    }

    @Test
    void test_updateCar() {
        //comportamiento del mapeador
        when(carMapper.car(carDTO)).thenReturn(car);
        when(carEntityMapper.carEntity(car)).thenReturn(carEntity);

        //llamar al metodo
        carService.updateCar(1, carDTO);

        //verificar interacción
        verify(carRepository).save(carEntity);
    }

}

