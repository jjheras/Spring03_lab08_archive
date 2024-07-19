package com.example.CarRegistry.service;

import com.example.CarRegistry.repository.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    void addUserImage(Long id, MultipartFile file);

    byte[] getUserImage(Long id);

    String carsCSV();

    void uploadCarsCSV(MultipartFile file);

}
