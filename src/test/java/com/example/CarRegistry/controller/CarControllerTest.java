package com.example.CarRegistry.controller;

import com.example.CarRegistry.controller.dto.BrandDTO;
import com.example.CarRegistry.controller.dto.CarBrandDTO;
import com.example.CarRegistry.controller.dto.CarDTO;
import com.example.CarRegistry.repository.BrandRepository;
import com.example.CarRegistry.repository.CarRepository;
import com.example.CarRegistry.repository.UserRepository;
import com.example.CarRegistry.service.BrandService;
import com.example.CarRegistry.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CarController carController;

    @MockBean
    private CarService carService;
    @MockBean
    private BrandService brandService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CarRepository carRepository;
    @MockBean
    private BrandRepository brandRepository;

    private CarDTO carDTO1;
    private CarDTO carDTO2;
    private BrandDTO brandDTO1;
    private BrandDTO brandDTO2;
    private CarBrandDTO carBrandDTO1;
    private CarBrandDTO carBrandDTO2;

    //configuación inicial
    @BeforeEach
    void setUp() {
        carDTO1 = new CarDTO();
        carDTO1.setId(1);
        carDTO1.setBrandid(1);
        carDTO1.setModel("Brand1");
        carDTO1.setYear(2020);
        carDTO1.setNumberofdoors(4);
        carDTO1.setIsconvertible(false);
        carDTO1.setMileage(15000);
        carDTO1.setPrice(20000.0);
        carDTO1.setDescription("Es un buen coche");
        carDTO1.setColour("rojo");
        carDTO1.setFueltype("Diesel");
        carDTO2 = new CarDTO(2, 2, "Brand2", 2021, 2, true, 10000, 30000.0, "descapotable", "azul", "diesel");

        brandDTO1 = new BrandDTO();
        brandDTO1.setId(1);
        brandDTO1.setName("testBrand1");
        brandDTO1.setWarranty(1);
        brandDTO1.setCountry("testCountry1");
        brandDTO2 = new BrandDTO(2, "testBrand2", 2, "testCountry2");

        carBrandDTO1 = new CarBrandDTO();
        carBrandDTO1.setId(1);
        carBrandDTO1.setBrand(brandDTO1);
        carBrandDTO1.setModel("Brand1");
        carBrandDTO1.setYear(2020);
        carBrandDTO1.setNumberofdoors(4);
        carBrandDTO1.setIsconvertible(false);
        carBrandDTO1.setMileage(15000);
        carBrandDTO1.setPrice(20000.0);
        carBrandDTO1.setDescription("Es un buen coche");
        carBrandDTO1.setColour("Rojo");
        carBrandDTO1.setFueltype("Diesel");
        carBrandDTO2 = new CarBrandDTO(2, brandDTO2, "Brand2", 2021, 2, true, 10000, 30000.0, "descapotable", "azul", "diesel");
    }

    //prueba para obtener todos los coches
    @Test
    @WithMockUser(roles = "CLIENT")
    void test_showCars() throws Exception{
        //lista de coches de prueba
        List<CarDTO> carDTOList = Arrays.asList(carDTO1,carDTO2);

        //simular la respuesta de service
        when(carService.getAllCars()).thenReturn(CompletableFuture.completedFuture(carDTOList));


        //esperar a que la tarea asínclora se complete
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())// Espera una respuesta  200
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(carDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brandid").value(carDTO1.getBrandid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value(carDTO1.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(carDTO1.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberofdoors").value(carDTO1.getNumberofdoors()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isconvertible").value(carDTO1.getIsconvertible()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mileage").value(carDTO1.getMileage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(carDTO1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(carDTO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].colour").value(carDTO1.getColour()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fueltype").value(carDTO1.getFueltype()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(carDTO2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].brandid").value(carDTO2.getBrandid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value(carDTO2.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(carDTO2.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberofdoors").value(carDTO2.getNumberofdoors()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isconvertible").value(carDTO2.getIsconvertible()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].mileage").value(carDTO2.getMileage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(carDTO2.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(carDTO2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].colour").value(carDTO2.getColour()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fueltype").value(carDTO2.getFueltype()));


    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void test_showCars_ko() throws Exception{
        //simular excepcion
        when(carService.getAllCars()).thenThrow(new RuntimeException("Error al obtener los coches"));

        //Realizar solicitud y verificar respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    //prueba para agregar lista de coches
    @Test
    @WithMockUser(roles = "VENDOR")
    void test_addCars() throws Exception {
        CompletableFuture<Void> completableFuture = CompletableFuture.completedFuture(null);

        //simular la respuesta de service
        when(carService.addCars(anyList())).thenReturn(completableFuture);

        //realizar solicitud POST y verificar respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addCars").contentType(MediaType.APPLICATION_JSON).content("[{ \"id\": 1, \"brandid\": 1, \"model\": \"Model1\", \"year\": 2020, \"numberofdoors\": 4, \"isconvertible\": false, \"mileage\": 15000, \"price\": 20000.0, \"description\": \"A nice car\", \"colour\": \"Red\", \"fueltype\": \"Petrol\" }, " + "{ \"id\": 2, \"brandid\": 2, \"model\": \"Model2\", \"year\": 2021, \"numberofdoors\": 2, \"isconvertible\": true, \"mileage\": 10000, \"price\": 30000.0, \"description\": \"A convertible car\", \"colour\": \"Blue\", \"fueltype\": \"Diesel\" }]")).andExpect(status().isOk());//espera respuesta 200
    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void test_addCars_ko() throws Exception{
        when(carService.addCars(anyList())).thenThrow(new RuntimeException("Error al añadir coches"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addCars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{ \"id\": 1, \"brandid\": 1, \"model\": \"Brand1\", \"year\": 2020, \"numberofdoors\": 4, \"isconvertible\": false, \"mileage\": 15000, \"price\": 20000.0, \"description\": \"Es un buen coche\", \"colour\": \"rojo\", \"fueltype\": \"Diesel\" }, { \"id\": 2, \"brandid\": 2, \"model\": \"Brand2\", \"year\": 2021, \"numberofdoors\": 2, \"isconvertible\": true, \"mileage\": 10000, \"price\": 30000.0, \"description\": \"descapotable\", \"colour\": \"azul\", \"fueltype\": \"diesel\" }]"))
                .andExpect(status().isInternalServerError());
    }

    //prueba de agragar un coche
    @Test
    @WithMockUser(roles = "VENDOR")
    void test_addCar() throws Exception {
        //simular llamada al service
        doNothing().when(carService).addCar(carDTO1);

        //realizar llamada POST y veriricar respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addCar").contentType(MediaType.APPLICATION_JSON).content("{ \"id\": 1, \"brandid\": 1, \"model\": \"Model1\", \"year\": 2020, \"numberofdoors\": 4, \"isconvertible\": false, \"mileage\": 15000, \"price\": 20000.0, \"description\": \"A nice car\", \"colour\": \"Red\", \"fueltype\": \"Petrol\" }")).andExpect(status().isOk());//espera respueta 200

    }
    @Test
    @WithMockUser(roles = "VENDOR")
    void test_addCar_ko() throws Exception {
        //simular llamada al service
        doThrow(new RuntimeException("Error al guardar el coche")).when(carService).addCar(any(CarDTO.class));

        //realizar llamada POST y veriricar respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"brandid\": 1, \"model\": \"Model1\", \"year\": 2020, \"numberofdoors\": 4, \"isconvertible\": false, \"mileage\": 15000, \"price\": 20000.0, \"description\": \"A nice car\", \"colour\": \"Red\", \"fueltype\": \"Petrol\" }"))
                .andExpect(status().isInternalServerError());

    }


    //prueba para eliminar un coche por id
    @Test
    @WithMockUser(roles = "VENDOR")
    void test_deleteCar() throws Exception {
        //simular la llamada a service
        doNothing().when(carService).deleteCarById(1);

        //realizar solicitud DELETE y verificar estado de respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/delCar/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "VENDOR")
    void test_deleteCar_ko() throws Exception {
        //simular la llamada a service
        doThrow(new RuntimeException("Error interno al eliminar el coche")).when(carService).deleteCarById(1);

        //realizar solicitud DELETE y verificar estado de respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/delCar/1")
                .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isInternalServerError());
    }

    //prueba para actualizar coche
    @Test
    @WithMockUser(roles = "VENDOR")
    void test_putCar() throws Exception {
        //Simular llamada a service
        doNothing().when(carService).updateCar(1, carDTO1);

        //REalizar solicitud PUT y verificar respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.put("/putCar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"brandid\": 1, \"model\": \"Model1\", \"year\": 2020, \"numberofdoors\": 4, \"isconvertible\": false, \"mileage\": 15000, \"price\": 20000.0, \"description\": \"A nice car\", \"colour\": \"Red\", \"fueltype\": \"Petrol\" }"))
                .andExpect(status().isOk());//espera repuesta 200
    }
    @Test
    @WithMockUser(roles = "VENDOR")
    void test_putCar_ko() throws Exception {
        //Simular llamada a service
        doThrow(new RuntimeException("Error interno al actualizar el coche")).when(carService).updateCar(any(Integer.class), any(CarDTO.class));

        //REalizar solicitud PUT y verificar respuesta
        this.mockMvc.perform(MockMvcRequestBuilders.put("/putCar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"brandid\": 1, \"model\": \"Model1\", \"year\": 2020, \"numberofdoors\": 4, \"isconvertible\": false, \"mileage\": 15000, \"price\": 20000.0, \"description\": \"A nice car\", \"colour\": \"Red\", \"fueltype\": \"Petrol\" }"))
                .andExpect(status().isInternalServerError());
    }

    //prueba para obtener un coche por id
    @Test
    @WithMockUser(roles = "CLIENT")
    void test_getCarId() throws Exception {
        //simular respuesta de servicio
        when(carService.getCarById(1)).thenReturn(carDTO1);

        //realizar solicitud GET y verificar estado y contenido
        this.mockMvc.perform(MockMvcRequestBuilders.get("/getCar/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(carDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandid").value(carDTO1.getBrandid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(carDTO1.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(carDTO1.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberofdoors").value(carDTO1.getNumberofdoors()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isconvertible").value(carDTO1.getIsconvertible()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mileage").value(carDTO1.getMileage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(carDTO1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(carDTO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.colour").value(carDTO1.getColour()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fueltype").value(carDTO1.getFueltype()));
    }
    @Test
    @WithMockUser(roles = "CLIENT")
    void test_getCarId_ko() throws Exception {
        //simular respuesta de servicio
        when(carService.getCarById(1)).thenThrow(new RuntimeException("No encontrado"));

        //realizar solicitud GET y verificar estado y contenido
        this.mockMvc.perform(MockMvcRequestBuilders.get("/getCar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    //prueba para obtener coches con amarcas
    @Test
    @WithMockUser(roles = "CLIENT")
    void test_getAllCarsBrand() throws Exception{
        //lista de coches de prueba
        List<CarBrandDTO> carBrandDTOList = Arrays.asList(carBrandDTO1,carBrandDTO2);

        //simular llamada a service
        when(carService.getAllCarBrand()).thenReturn(CompletableFuture.completedFuture(carBrandDTOList));


        // Esperar a que la tarea asíncrona
        this.mockMvc.perform(MockMvcRequestBuilders.get("/carsBrand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())// Espera una respuesta  200
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(carBrandDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value(carBrandDTO1.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(carBrandDTO1.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberofdoors").value(carBrandDTO1.getNumberofdoors()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isconvertible").value(carBrandDTO1.getIsconvertible()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mileage").value(carBrandDTO1.getMileage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(carBrandDTO1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(carBrandDTO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].colour").value(carBrandDTO1.getColour()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fueltype").value(carBrandDTO1.getFueltype()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand.id").value(carBrandDTO1.getBrand().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(carBrandDTO2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value(carBrandDTO2.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(carBrandDTO2.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberofdoors").value(carBrandDTO2.getNumberofdoors()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isconvertible").value(carBrandDTO2.getIsconvertible()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].mileage").value(carBrandDTO2.getMileage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(carBrandDTO2.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(carBrandDTO2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].colour").value(carBrandDTO2.getColour()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fueltype").value(carBrandDTO2.getFueltype()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].brand.id").value(carBrandDTO2.getBrand().getId()));

    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void test_getAllCarsBrand_ko() throws Exception {
        when(carService.getAllCarBrand()).thenThrow(new RuntimeException("Error al obtener todos los coches con sus marcas"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/carsBrand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }


    //prueba para obtener coche y su marca por id
    @Test
    @WithMockUser(roles = "CLIENT")
    void test_getCarBrand() throws Exception {
        //simular llama a servicio
        when(carService.getCarBrandById(1)).thenReturn(carBrandDTO1);

        //Realizar solicitud GET y verificar estado y contenido.
        this.mockMvc.perform(MockMvcRequestBuilders.get("/carBrand/1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(carBrandDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(carBrandDTO1.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(carBrandDTO1.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberofdoors").value(carBrandDTO1.getNumberofdoors()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isconvertible").value(carBrandDTO1.getIsconvertible()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mileage").value(carBrandDTO1.getMileage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(carBrandDTO1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(carBrandDTO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.colour").value(carBrandDTO1.getColour()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fueltype").value(carBrandDTO1.getFueltype()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.id").value(carBrandDTO1.getBrand().getId()));


    }
    @Test
    @WithMockUser(roles = "CLIENT")
    void test_getCarBrand_ko() throws Exception {
        when(carService.getCarBrandById(1)).thenThrow(new RuntimeException("Error interno al obtener el coche con marca por id: 1"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/carBrand/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }

}
