package com.helios.platform.sentinel.repository;

import com.helios.platform.sentinel.models.OwnerRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISentinelRepository extends JpaRepository<OwnerRecordModel, Long> {
    boolean existsByUuid(String uuid);

    @Query("SELECT o FROM OwnerRecordModel o WHERE o.fecha_acceso BETWEEN :startDate AND :endDate")
    List<OwnerRecordModel> queryData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT o FROM OwnerRecordModel o WHERE o.fecha_acceso BETWEEN :startDate AND :endDate " +
           "AND (:filterType IS NULL OR :filterType = '' OR LOWER(o.type) = LOWER(:filterType)) " +
           "AND (:filterProperty IS NULL OR :filterProperty = '' OR LOWER(o.propertyID) = LOWER(:filterProperty)) " +
           "AND (:searchTerm IS NULL OR :searchTerm = '' OR LOWER(o.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(o.apellido) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(o.cedula) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(o.propertyID) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    org.springframework.data.domain.Page<OwnerRecordModel> queryDataPaginated(
            @Param("startDate") String startDate, @Param("endDate") String endDate,
            @Param("filterType") String filterType, @Param("filterProperty") String filterProperty,
            @Param("searchTerm") String searchTerm,
            org.springframework.data.domain.Pageable pageable);

    @Query(value = "SELECT " +
            "COALESCE(SUM(c.qty_people), 0) AS total_general, " +
            "COALESCE(SUM(CASE WHEN c.type = 'Propietario' THEN c.qty_people ELSE 0 END), 0) AS total_owners, " +
            "COALESCE(SUM(CASE WHEN c.type = 'Invitado' THEN c.qty_people ELSE 0 END), 0) AS total_guest, " +
            "COALESCE(SUM(CASE WHEN c.type = 'Alquilado' THEN c.qty_people ELSE 0 END), 0) AS total_rented, " +
            "COALESCE(SUM(CASE WHEN c.type = 'Autorizado' THEN c.qty_people ELSE 0 END), 0) AS total_authorized " +
            "FROM cac_access_data c WHERE c.fecha_acceso BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Object[]> getAllResumedCounts(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
