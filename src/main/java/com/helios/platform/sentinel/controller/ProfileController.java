package com.helios.platform.sentinel.controller;

import com.helios.platform.sentinel.dto.UserProfileDTO;
import com.helios.platform.sentinel.dto.UserResponseDTO;
import com.helios.platform.sentinel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserResponseDTO profile = userService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateProfile(Authentication authentication, @RequestBody UserProfileDTO dto) {
        String username = authentication.getName();
        UserResponseDTO updatedProfile = userService.updateProfile(username, dto);
        return ResponseEntity.ok(updatedProfile);
    }
}
