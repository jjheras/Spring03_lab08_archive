package com.example.CarRegistry.controller.mapper;

import com.example.CarRegistry.controller.dto.CarDTO;
import com.example.CarRegistry.service.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDTO carDto(Car car);

    Car car(CarDTO carDto);
}
