package com.example.CarRegistry.service.impl;

import com.example.CarRegistry.repository.UserRepository;
import com.example.CarRegistry.repository.entity.UserEntity;
import com.example.CarRegistry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    //Guardar ususarios
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no encontrado: " + email));
    }
}
