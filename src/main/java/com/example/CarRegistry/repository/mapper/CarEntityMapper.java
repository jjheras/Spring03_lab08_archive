package com.example.CarRegistry.repository.mapper;

import com.example.CarRegistry.repository.entity.CarEntity;
import com.example.CarRegistry.service.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CarEntityMapper {
    CarEntityMapper INSTANCE = Mappers.getMapper(CarEntityMapper.class);

    CarEntity carEntity(Car car);

    Car car(CarEntity carEntity);
}
