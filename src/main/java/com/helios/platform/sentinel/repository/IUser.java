package com.helios.platform.sentinel.repository;

import com.helios.platform.sentinel.models.Role;
import com.helios.platform.sentinel.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUser extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    long countByRole(Role role);
}
