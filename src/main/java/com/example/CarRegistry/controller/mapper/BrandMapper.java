package com.example.CarRegistry.controller.mapper;

import com.example.CarRegistry.controller.dto.BrandDTO;
import com.example.CarRegistry.service.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    BrandDTO brandDto(Brand brand);

    Brand brand(BrandDTO brandDTO);
}
