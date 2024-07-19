package com.example.CarRegistry.repository.mapper;

import com.example.CarRegistry.repository.entity.BrandEntity;
import com.example.CarRegistry.service.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BrandEntityMapper {
    BrandEntityMapper INSTANCE = Mappers.getMapper(BrandEntityMapper.class);

    BrandEntity brandEntity(Brand brand);

    Brand brand(BrandEntity brandEntity);

}
