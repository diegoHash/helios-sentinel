package com.helios.platform.sentinel.service;

import com.helios.platform.sentinel.dto.UserAdminDTO;
import com.helios.platform.sentinel.dto.UserProfileDTO;
import com.helios.platform.sentinel.dto.UserResponseDTO;
import com.helios.platform.sentinel.models.Role;
import com.helios.platform.sentinel.models.UserEntity;
import com.helios.platform.sentinel.repository.IUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUser userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserAdminDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public UserAdminDTO createUser(UserAdminDTO userDto) {
        if (userDto.getRole() == Role.ROOT && userRepository.countByRole(Role.ROOT) > 0) {
            throw new RuntimeException("Solo puede haber un usuario ROOT.");
        }
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya está en uso.");
        }

        UserEntity user = new UserEntity(
                userDto.getUsername(),
                userDto.getNombre(),
                userDto.getCorreo(),
                passwordEncoder.encode(userDto.getPin()),
                userDto.getRole(),
                userDto.getPermissions()
        );
        user.setActive(userDto.isActive());
        userRepository.save(user);
        return mapToDTO(user);
    }

    public UserAdminDTO updateUser(Long id, UserAdminDTO userDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (userDto.getRole() == Role.ROOT && user.getRole() != Role.ROOT) {
            if (userRepository.countByRole(Role.ROOT) > 0) {
                throw new RuntimeException("Solo puede haber un usuario ROOT.");
            }
        }

        user.setNombre(userDto.getNombre());
        user.setCorreo(userDto.getCorreo());
        user.setRole(userDto.getRole());
        user.setPermissions(userDto.getPermissions());
        user.setActive(userDto.isActive());

        if (userDto.getPin() != null && !userDto.getPin().isEmpty()) {
            user.setPin(passwordEncoder.encode(userDto.getPin()));
        }

        userRepository.save(user);
        return mapToDTO(user);
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (user.getRole() == Role.ROOT) {
            throw new RuntimeException("El usuario ROOT no puede ser eliminado.");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO updateProfile(String username, UserProfileDTO dto) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (dto.getNombre() != null) user.setNombre(dto.getNombre());
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
            user.setUsername(dto.getUsername());
        }
        if (dto.getProfileIcon() != null) user.setProfileIcon(dto.getProfileIcon());
        if (dto.getTheme() != null) user.setTheme(dto.getTheme());
        if (dto.getMouseGlow() != null) user.setMouseGlow(dto.getMouseGlow());

        userRepository.save(user);

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nombre(user.getNombre())
                .role(user.getRole().name())
                .permissions(user.getPermissions())
                .profileIcon(user.getProfileIcon())
                .theme(user.getTheme())
                .mouseGlow(user.isMouseGlow())
                .build();
    }

    public UserResponseDTO getProfile(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nombre(user.getNombre())
                .role(user.getRole().name())
                .permissions(user.getPermissions())
                .profileIcon(user.getProfileIcon())
                .theme(user.getTheme())
                .mouseGlow(user.isMouseGlow())
                .build();
    }

    private UserAdminDTO mapToDTO(UserEntity user) {
        UserAdminDTO dto = new UserAdminDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNombre(user.getNombre());
        dto.setCorreo(user.getCorreo());
        dto.setRole(user.getRole());
        dto.setPermissions(user.getPermissions());
        dto.setActive(user.isActive());
        return dto;
    }
}
