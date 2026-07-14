package com.helios.platform.sentinel.security;

import com.helios.platform.sentinel.models.Role;
import com.helios.platform.sentinel.models.UserEntity;
import com.helios.platform.sentinel.repository.IUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "application.bootstrap.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private final IUser userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;
    private final String permissions;

    public DataSeeder(
            IUser userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${application.bootstrap.username}") String username,
            @Value("${application.bootstrap.name}") String name,
            @Value("${application.bootstrap.email}") String email,
            @Value("${application.bootstrap.password}") String password,
            @Value("${application.bootstrap.role}") Role role,
            @Value("${application.bootstrap.permissions}") String permissions) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = requireValue("application.bootstrap.username", username);
        this.name = requireValue("application.bootstrap.name", name);
        this.email = requireValue("application.bootstrap.email", email);
        this.password = requirePassword(password);
        this.role = role;
        this.permissions = requireValue("application.bootstrap.permissions", permissions);
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Administrative bootstrap refused: username already exists");
        }

        UserEntity bootstrapUser = new UserEntity(
                username,
                name,
                email,
                passwordEncoder.encode(password),
                role,
                permissions
        );
        userRepository.save(bootstrapUser);
    }

    private static String requireValue(String property, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(property + " is required when administrative bootstrap is enabled");
        }
        return value.trim();
    }

    private static String requirePassword(String value) {
        String password = requireValue("application.bootstrap.password", value);
        if (password.length() < 12) {
            throw new IllegalStateException("application.bootstrap.password must contain at least 12 characters");
        }
        return password;
    }
}
