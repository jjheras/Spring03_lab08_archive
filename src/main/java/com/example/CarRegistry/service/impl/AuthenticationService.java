package com.example.CarRegistry.service.impl;

import com.example.CarRegistry.controller.dto.JwtResponse;
import com.example.CarRegistry.controller.dto.LoginRequest;
import com.example.CarRegistry.controller.dto.SingUpRequest;
import com.example.CarRegistry.repository.UserRepository;
import com.example.CarRegistry.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //Sirve para registrar a un usuario
    public JwtResponse singup(SingUpRequest request) throws BadRequestException {
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null || request.getRole() == null) {
            throw new BadRequestException("Nombre, email, contraseña y role son obligatorios");
        }
        var user = UserEntity
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtResponse.builder().token(jwt).build();
    }

    //sirve para logear usuarios y devolver el token.
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("email or password invalido."));
        var jwt = jwtService.generateToken(user);
        return JwtResponse.builder().token(jwt).build();
    }
}
