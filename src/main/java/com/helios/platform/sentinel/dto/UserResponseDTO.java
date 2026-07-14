package com.helios.platform.sentinel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String role;
    private String permissions;
    private String nombre;
    private String profileIcon;
    private String theme;
    private boolean mouseGlow;
}
