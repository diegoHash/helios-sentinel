package com.helios.platform.sentinel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String nombre;
    private String username;
    private String profileIcon;
    private String theme;
    private Boolean mouseGlow;
}
