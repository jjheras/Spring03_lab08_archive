package com.example.CarRegistry.service;

import com.example.CarRegistry.repository.entity.UserEntity;

public interface UserService {
    public UserEntity save(UserEntity newUser);
}
