package com.example.CarRegistry.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)//No pueden tener el mismo email para autenticarlos.
    String email;

    String password;
    String role;
    String img;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }//devuelve el nombre de usuario

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }//Si la cuenta esta caducada

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }//Si el usuario esta bloqueada

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }//Si las credenciales han espirado

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }//Si esta activo
}
