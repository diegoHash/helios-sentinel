package com.helios.platform.sentinel.dto;

import com.helios.platform.sentinel.models.Role;
import lombok.Data;

@Data
public class UserAdminDTO {
    private Long id;
    private String username;
    private String nombre;
    private String correo;
    private String pin; // Only sent when creating or updating pin
    private Role role;
    private String permissions;
    private boolean active;
}
