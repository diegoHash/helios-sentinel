package com.helios.platform.sentinel.controller;

import com.helios.platform.sentinel.dto.AuthRequestDTO;
import com.helios.platform.sentinel.dto.AuthResponseDTO;
import com.helios.platform.sentinel.dto.RecoverRequestDTO;
import com.helios.platform.sentinel.dto.RegisterRequestDTO;
import com.helios.platform.sentinel.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/recover")
    public ResponseEntity<String> recoverPassword(@RequestBody RecoverRequestDTO request) {
        service.recoverPassword(request);
        return ResponseEntity.ok("Password successfully updated");
    }
}
