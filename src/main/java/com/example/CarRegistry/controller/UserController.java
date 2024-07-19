package com.example.CarRegistry.controller;

import com.example.CarRegistry.controller.dto.LoginRequest;
import com.example.CarRegistry.controller.dto.SingUpRequest;
import com.example.CarRegistry.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")//dar de alta usuarios
    public ResponseEntity<?> signup(@RequestBody SingUpRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.singup(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/login")//logear usuarios y obtener el token
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
