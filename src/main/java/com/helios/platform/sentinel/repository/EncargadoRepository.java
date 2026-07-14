package com.helios.platform.sentinel.repository;

import com.helios.platform.sentinel.models.EncargadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncargadoRepository extends JpaRepository<EncargadoModel, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT e FROM EncargadoModel e WHERE e.propietario.num_property = ?1")
    java.util.List<EncargadoModel> findByPropietarioNumProperty(String numProperty);
}
