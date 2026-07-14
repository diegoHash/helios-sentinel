package com.helios.platform.sentinel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import com.helios.platform.sentinel.security.CryptoConverter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caribbean_one_users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "operator_name", nullable = false)
    private String nombre;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "email", nullable = false, unique = true)
    private String correo;

    @Column(name = "password", nullable = false)
    private String pin;

    @Transient
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "active";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String permissions;

    @Column(name = "origin_app", nullable = false)
    @Builder.Default
    private String originApp = "CAC";

    @Transient
    @Builder.Default
    private String profileIcon = "User";

    @Transient
    @Builder.Default
    private String theme = "dark";

    @Transient
    @Builder.Default
    private boolean mouseGlow = true;

    public UserEntity(String username, String nombre, String correo, String pin, Role role, String permissions) {
        this.username = username;
        this.nombre = nombre;
        this.correo = correo;
        this.pin = pin;
        this.role = role;
        this.permissions = permissions;
        this.uuid = UUID.randomUUID();
        this.status = "active";
        this.originApp = "CAC";
    }

    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    public void setActive(boolean active) {
        this.status = active ? "active" : "disabled";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return pin;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
