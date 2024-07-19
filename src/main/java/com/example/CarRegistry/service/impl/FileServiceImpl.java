package com.example.CarRegistry.service.impl;

import com.example.CarRegistry.repository.BrandRepository;
import com.example.CarRegistry.repository.CarRepository;
import com.example.CarRegistry.repository.UserRepository;
import com.example.CarRegistry.repository.entity.CarEntity;
import com.example.CarRegistry.repository.entity.UserEntity;
import com.example.CarRegistry.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    //Este merodo sirve para subir una imagen a un usuario ya existente.
    @Override
    public void addUserImage(Long id, MultipartFile file)  {
        try {
            Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
            if(optionalUserEntity.isPresent()){
                UserEntity entity = optionalUserEntity.get();
                log.info("grabando imagen.");
                entity.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
                userRepository.save(entity);
            }else {
                log.warn("El Id del usuario no existe: "+id);
                throw new RuntimeException("El ID del usuario no existe: "+id);
            }

        }catch (IOException e){
            log.error("Error al guardar la imagen ", e);
            throw new RuntimeException("Error al guardar la imagen ", e);
        }

    }

    //obtener la imagen de un usuario

    @Override
    public byte[] getUserImage(Long id){
        UserEntity entity  = userRepository.findById(id).orElseThrow(()-> new RuntimeException("El ID del usuario no existe: " + id));
        if(entity.getImg() == null){
            throw new RuntimeException("El usuario no tiene una imagen.");
        }
        return Base64.getDecoder().decode(entity.getImg());
    }
    //Sirve para subir un registro de coches desde un CSV
    @Override
    @Async("taskExecutor")
    public void uploadCarsCSV(MultipartFile file) {
        List<CarEntity> carList = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for(CSVRecord record : csvRecords){
                CarEntity car = new CarEntity();
                car.setBrandid(Integer.valueOf(record.get("brandid")));
                car.setModel(record.get("model"));
                car.setYear(Integer.valueOf(record.get("year")));
                car.setNumberofdoors(Integer.parseInt(record.get("numberofdoors")));
                car.setIsconvertible(Boolean.parseBoolean(record.get("isconvertible")));
                car.setMileage(Integer.parseInt(record.get("mileage")));
                car.setPrice(Double.parseDouble(record.get("price")));
                car.setDescription(record.get("description"));
                car.setColour(record.get("colour"));
                car.setFueltype(record.get("fueltype"));

                carList.add(car);
            }
            carRepository.saveAll(carList);

        }catch (Exception e){
            log.error("Error al guardar usuarios");
            throw new RuntimeException("Error al guarda los coches dedsde CSV", e);

        }
    }
    //obtener de la BBDD los registros
    @Override
    public String carsCSV() {
        List<CarEntity> carList = carRepository.findAll();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("id;brandid;model;year;numberofdoors;isconvertible;mileage;price;description;colour;fueltype\n");
        for(CarEntity car : carList){
            csvContent.append(String.format("%d;%d;%s;%d;%d;%b;%d;%.2f;%s;%s;%s\n",
                    car.getId(),
                    car.getBrandid(),
                    car.getModel(),
                    car.getYear(),
                    car.getNumberofdoors(),
                    car.getIsconvertible(),
                    car.getMileage(),
                    car.getPrice(),
                    car.getDescription(),
                    car.getColour(),
                    car.getFueltype()));
        }
        return csvContent.toString();
    }
}
