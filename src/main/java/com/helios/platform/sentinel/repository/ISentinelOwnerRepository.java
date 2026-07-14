package com.helios.platform.sentinel.repository;

import com.helios.platform.sentinel.models.PropietarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISentinelOwnerRepository extends JpaRepository<PropietarioModel, Long> {

    @Query(value = "SELECT p.num_property FROM PropietarioModel p", nativeQuery = false)
    List<String> findProperties();

    @Query(value = "SELECT COUNT(p.num_property) FROM PropietarioModel p", nativeQuery = false)
    Long countProperties();

    @Query(value = "SELECT p FROM PropietarioModel p WHERE p.num_property = ?1", nativeQuery = false)
    java.util.Optional<PropietarioModel> findByNumProperty(String numProperty);
}
