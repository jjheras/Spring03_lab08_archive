package com.example.CarRegistry.filter;

import com.example.CarRegistry.service.impl.JwtService;
import com.example.CarRegistry.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserServiceImpl userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        //Comprobar la información
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7); // por defecto las cabecestas comienzan con Bearer XXXX
        log.info("JWT = {}", jwt);
        userEmail = jwtService.extractUserName(jwt);//recumeramos el email

        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {//si el token es valido
                log.info("User - {}", userDetails);
                List<SimpleGrantedAuthority> authorities = jwtService.extractRoles(jwt).stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext context = SecurityContextHolder.createEmptyContext();//contesto vacio
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }

        }
        filterChain.doFilter(request, response);

    }
}
