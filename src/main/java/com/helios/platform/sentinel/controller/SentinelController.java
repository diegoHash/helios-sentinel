package com.helios.platform.sentinel.controller;

import com.helios.platform.sentinel.error.TelegramNotificationError;
import com.helios.platform.sentinel.models.OwnerRecordModel;
import com.helios.platform.sentinel.models.PropietarioModel;
import com.helios.platform.sentinel.service.SentinelOwnerService;
import com.helios.platform.sentinel.service.SentinelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping("/")
public class SentinelController {

    @Autowired
    private final SentinelService service;

    @Autowired
    private final SentinelOwnerService cacOwnerService;

    private final TelegramNotificationError telegramNotificationError;


    private List<String> ownerList;

    public SentinelController(SentinelService service, SentinelOwnerService cacOwnerService,
                          TelegramNotificationError telegramNotificationError) {
        this.service = service;
        this.cacOwnerService = cacOwnerService;
        this.telegramNotificationError = telegramNotificationError;
        ownerList = cacOwnerService.getPropertiesID();
    }

    @PostMapping("/save")
    public ResponseEntity saveRecord(@RequestBody OwnerRecordModel recordModel, HttpServletRequest request) {

        if (!ownerList.contains(recordModel.getPropertyID())) {
            return ResponseEntity.badRequest().body("NOT A VALID PROPERTY ID");
        }
        for (Field attributes : recordModel.getClass().getDeclaredFields()) {
            attributes.setAccessible(true);
            if (!attributes.getName().equals("id") && !attributes.getName().equals("vehiculos")) {
                Object value = null;
                try {
                    value = attributes.get(recordModel);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (value == null) return ResponseEntity.badRequest().body("Invalid fields");
                if (attributes.getType().equals(String.class)) {
                    if (String.valueOf(value).isEmpty() || String.valueOf(value).isBlank() )
                        return ResponseEntity.badRequest().body("Invalid fields");
                }
            }
        }

        if (recordModel.getQtyPeople() <= 0) {
            return ResponseEntity.badRequest().body("Invalid fields");
        }
        service.saveRecord(recordModel);
        StringBuilder sb = new StringBuilder();
        sb.append("Inmueble: " + recordModel.getPropertyID());
        sb.append("\n");
        sb.append("Cantidad: " + recordModel.getQtyPeople());
        sb.append("\n");
        sb.append("Fecha: " + recordModel.getFecha_acceso());
        sb.append("\n");
        sb.append("Sector: " + recordModel.getBuildingName());
        sb.append("\n");
        sb.append("Condicion: " + recordModel.getType());
        sb.append("\n");
        if (recordModel.getVehiculos() != null && !recordModel.getVehiculos().isEmpty()) {
            sb.append("Vehículos (" + recordModel.getVehiculos().size() + "):\n");
            for (com.helios.platform.sentinel.models.VehiculoModel v : recordModel.getVehiculos()) {
                sb.append(" - " + v.getMarca() + " " + v.getModelo() + " (" + v.getColor() + "), Placa: " + v.getPlaca() + "\n");
            }
        }
        telegramNotificationError.sendMessage(sb.toString());
        return ResponseEntity.ok().body("SUCCESS OPERATION");
    }

    /*
    @PostMapping("/download")
    public


     */

    @PostMapping("/saveAll")
    public ResponseEntity saveRecord(@RequestBody List<OwnerRecordModel> recordModel) {

        for (OwnerRecordModel record : recordModel) {
            if (!ownerList.contains(record.getPropertyID())) {
                return ResponseEntity.badRequest().body("THERE ISN'T NOT A VALID PROPERTY ID "
                        + record.getPropertyID());
            }
        }
        return service.saveAllRecords(recordModel);
    }

    @DeleteMapping("/dv/{id}")
    public ResponseEntity<String> deletebyId(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.ok("Deleted successfully!");
    }

    @GetMapping("/getreportdetailed/{startdate}")
    public ResponseEntity<Object> getReport(@PathVariable String startdate) {
        String enddate = startdate + " 23:59:59";
        String initialDate = startdate;
        startdate = startdate + " 00:00:00";

        java.util.Map<String, Object> reportMap = service.getReportToday(startdate, enddate);
        try {
            String jsonString = new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(reportMap);
            telegramNotificationError.sendReportAsFile(jsonString);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(reportMap);
    }
//
//    @GetMapping("/todayreporttelegram/{startdate}")
//    public ResponseEntity<String> getReportTelegram(@PathVariable String startdate) {
//        String enddate = startdate + " 23:59";
//        String initialDate = startdate;
//        startdate = startdate + " 00:00";
//        telegramNotificationError.sendMessage("Para la fecha: " + initialDate + service.getReportTodayTelegram(startdate,enddate));
//        return ResponseEntity.ok("Hecho!");
//    }

    @GetMapping("/owners")
    public List<PropietarioModel> getOwners(HttpServletRequest request) {
        return cacOwnerService.getOwners();
    }

    @GetMapping("/home")
    public String home() {
        return "Hello World from server v1.1!";
    }

    @GetMapping("/dashboard/{date}")
    public ResponseEntity<List<OwnerRecordModel>> getDashboardData(@PathVariable String date) {
        String startdate = date + " 00:00:00";
        String enddate = date + " 23:59:59";
        return ResponseEntity.ok(service.getRecordsByDateRange(startdate, enddate));
    }

    @GetMapping("/dashboard/range")
    public ResponseEntity<List<OwnerRecordModel>> getDashboardDataRange(@RequestParam String startDate, @RequestParam String endDate) {
        String startdate = startDate + " 00:00:00";
        String enddate = endDate + " 23:59:59";
        return ResponseEntity.ok(service.getRecordsByDateRange(startdate, enddate));
    }

    @GetMapping("/dashboard/range/page")
    public ResponseEntity<org.springframework.data.domain.Page<OwnerRecordModel>> getDashboardDataRangePaginated(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String filterProperty,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        String startdate = startDate + " 00:00:00";
        String enddate = endDate + " 23:59:59";
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(service.getRecordsByDateRangePaginated(startdate, enddate, filterType, filterProperty, searchTerm, pageable));
    }
}
