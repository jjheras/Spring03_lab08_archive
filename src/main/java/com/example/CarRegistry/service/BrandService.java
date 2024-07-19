package com.example.CarRegistry.service;

import com.example.CarRegistry.controller.dto.BrandDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BrandService {
    CompletableFuture<List<BrandDTO>> getAllBrands();

    void addBrand(BrandDTO brandDto);

    void deleteBrandById(Integer id);

    BrandDTO getBrandById(Integer id);

    void updateBrand(Integer id, BrandDTO brandDto);

    CompletableFuture<Void> addBrands(List<BrandDTO> brandDtos);
}
