package com.helios.platform.sentinel.security;

import lombok.RequiredArgsConstructor;
import com.helios.platform.sentinel.dto.AuthRequestDTO;
import com.helios.platform.sentinel.dto.AuthResponseDTO;
import com.helios.platform.sentinel.dto.RecoverRequestDTO;
import com.helios.platform.sentinel.dto.RegisterRequestDTO;
import com.helios.platform.sentinel.dto.UserResponseDTO;
import com.helios.platform.sentinel.models.Role;
import com.helios.platform.sentinel.models.UserEntity;
import com.helios.platform.sentinel.repository.IUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUser repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Role assignedRole;
        if (repository.countByRole(Role.ROOT) == 0) {
            assignedRole = Role.ROOT;
        } else {
            // Check if current user is ROOT
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ROOT"))) {
                throw new RuntimeException("Forbidden: Sólo el usuario ROOT puede crear cuentas");
            }
            assignedRole = Role.OS;
        }

        var user = new UserEntity(
                request.getUsername(),
                request.getNombre(),
                request.getCorreo(),
                passwordEncoder.encode(request.getPin()),
                assignedRole,
                request.getPermissions()
        );
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole().name())
                .user(UserResponseDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .permissions(user.getPermissions())
                        .nombre(user.getNombre())
                        .profileIcon(user.getProfileIcon())
                        .theme(user.getTheme())
                        .mouseGlow(user.isMouseGlow())
                        .build())
                .build();
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole().name())
                .user(UserResponseDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .permissions(user.getPermissions())
                        .nombre(user.getNombre())
                        .profileIcon(user.getProfileIcon())
                        .theme(user.getTheme())
                        .mouseGlow(user.isMouseGlow())
                        .build())
                .build();
    }

    public void recoverPassword(RecoverRequestDTO request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Removed recover logic as secretPhrase is gone. Or use email/PIN recovery later.
        throw new RuntimeException("Recovery not implemented");
    }
}
