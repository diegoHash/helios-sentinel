package com.helios.platform.sentinel.service;

import com.helios.platform.sentinel.models.OwnerRecordModel;
import com.helios.platform.sentinel.repository.ISentinelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentinelService {
    ISentinelRepository cac_repository;

    Integer count = 0;

    public SentinelService(ISentinelRepository cac_repository) {
        this.cac_repository = cac_repository;
    }


    public ResponseEntity<String> saveRecord(OwnerRecordModel recordModel) {
        if (recordModel.getUuid() != null && cac_repository.existsByUuid(recordModel.getUuid())) {
            return ResponseEntity.ok("Already exists (Idempotent)");
        }
        cac_repository.save(recordModel);
        return ResponseEntity.ok("OK");
    }

    public ResponseEntity<String> saveAllRecords(List<OwnerRecordModel> records) {
        List<OwnerRecordModel> toSave = records.stream()
            .filter(r -> r.getUuid() == null || !cac_repository.existsByUuid(r.getUuid()))
            .toList();

        if (!toSave.isEmpty()) {
            cac_repository.saveAll(toSave);
        }
        return ResponseEntity.ok("Success");
    }

    public java.util.Map<String, Object> getReportToday(String startDate, String endDate) {
        List<OwnerRecordModel> reportToday = cac_repository.queryData(startDate, endDate);
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        response.put("summary", getCountResumed(startDate, endDate, reportToday.size()));
        response.put("detailedRecords", reportToday);

        return response;
    }

    public java.util.Map<String, Object> getCountResumed(String startdate, String enddate, int totalInmuebles) {
        List<Object[]> counts = cac_repository.getAllResumedCounts(startdate, enddate);
        java.util.Map<String, Object> summary = new java.util.HashMap<>();

        if (counts != null && !counts.isEmpty() && counts.get(0) != null) {
            Object[] row = counts.get(0);
            summary.put("total_general", row[0] != null ? ((Number) row[0]).intValue() : 0);
            summary.put("total_owners", row[1] != null ? ((Number) row[1]).intValue() : 0);
            summary.put("total_guest", row[2] != null ? ((Number) row[2]).intValue() : 0);
            summary.put("total_rented", row[3] != null ? ((Number) row[3]).intValue() : 0);
            summary.put("total_authorized", row[4] != null ? ((Number) row[4]).intValue() : 0);
        } else {
            summary.put("total_general", 0);
            summary.put("total_owners", 0);
            summary.put("total_guest", 0);
            summary.put("total_rented", 0);
            summary.put("total_authorized", 0);
        }
        summary.put("total_inmuebles_ocupados", totalInmuebles);

        return summary;
    }

    public void deleteById(Long id) {
        Boolean exist = cac_repository.findById(id).isPresent();
        if (exist)cac_repository.deleteById(id);
    }

    public List<OwnerRecordModel> getRecordsByDateRange(String startDate, String endDate) {
        return cac_repository.queryData(startDate, endDate);
    }

    public org.springframework.data.domain.Page<OwnerRecordModel> getRecordsByDateRangePaginated(String startDate, String endDate, String filterType, String filterProperty, String searchTerm, org.springframework.data.domain.Pageable pageable) {
        return cac_repository.queryDataPaginated(startDate, endDate, filterType, filterProperty, searchTerm, pageable);
    }
}
