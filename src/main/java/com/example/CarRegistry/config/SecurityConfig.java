package com.example.CarRegistry.config;

import com.example.CarRegistry.filter.JwtAuthenticationFilter;
import com.example.CarRegistry.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    //Autentificamos a los usuarios
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);//Recuperamos el userService
        authProvider.setPasswordEncoder(passwordEncoder);//Codificamos la cotraseña
        return authProvider;
    }

    //Devuelve la configuración actual para utilizarla
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Cdena de conexión de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)//desabilita las peticiones cruzadas
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Maneja las sesiones
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login", "/signup", "/addBrand", "/addBrands", "/addCar", "/addCars","/userImage/{id}/add","/uploadCarsCSV").permitAll()
                        .requestMatchers(HttpMethod.GET, "/brand", "/getBrand/{id}", "/cars", "/getCar/{id}", "/carsBrand", "/carBrand/{id}","/downloadCarsCSV","/userImage/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/delBrand/{id}", "/delCar/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/putBrand/{id}", "/putCar/{id}").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
