package com.example.CarRegistry.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${token.secret.key}")//para firmar el token
    private String jwtSecretKey;

    @Value("${token.expirationms}")//tiempo de caducidad del token
    private Long jwtExpiratioMs;

    //Extrae el nombre de usuario del token JWT.
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Genera un token.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Agrega los roles del usuario como claims al token
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList()));
        return generateToken(claims, userDetails);
    }

    //Verifica si el token es válido.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //Extrae un claim específico del token.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Genera un token con claims adicionales del usuario.
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiratioMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    //Verifica si el token JWT ha expirado.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrae la fecha de expiración del token.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Extrae todos los claims del token.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    //Obtiene la  firma.
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Extrae los roles del token.
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));

    }
}
