package com.helios.platform.sentinel.repository;

import com.helios.platform.sentinel.models.CargaFamiliarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargaFamiliarRepository extends JpaRepository<CargaFamiliarModel, Long> {
    List<CargaFamiliarModel> findByInmueble(String inmueble);

    @org.springframework.data.jpa.repository.Query("SELECT c FROM CargaFamiliarModel c WHERE c.propietario.num_property = ?1")
    List<CargaFamiliarModel> findByPropietarioNumProperty(String numProperty);
}
