package com.helios.platform.sentinel.controller;


import com.helios.platform.sentinel.service.CargaFamiliarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carga-familiar")
public class CargaFamiliarController {

    @Autowired
    private CargaFamiliarService cargaFamiliarService;

    @Autowired
    private com.helios.platform.sentinel.repository.CargaFamiliarRepository cargaFamiliarRepository;

    @Autowired
    private com.helios.platform.sentinel.repository.EncargadoRepository encargadoRepository;

    @GetMapping("/all")
    public ResponseEntity<java.util.List<com.helios.platform.sentinel.models.CargaFamiliarModel>> getAllCargaFamiliar() {
        return ResponseEntity.ok(cargaFamiliarService.obtenerTodos());
    }

    @GetMapping("/encargados/all")
    public ResponseEntity<java.util.List<com.helios.platform.sentinel.models.EncargadoModel>> getAllEncargados() {
        return ResponseEntity.ok(encargadoRepository.findAll());
    }

    @GetMapping("/by-property/{numProperty}")
    public ResponseEntity<java.util.List<com.helios.platform.sentinel.models.CargaFamiliarModel>> getCargaFamiliarByProperty(@PathVariable String numProperty) {
        return ResponseEntity.ok(cargaFamiliarRepository.findByPropietarioNumProperty(numProperty));
    }

    @GetMapping("/encargados/by-property/{numProperty}")
    public ResponseEntity<java.util.List<com.helios.platform.sentinel.models.EncargadoModel>> getEncargadosByProperty(@PathVariable String numProperty) {
        return ResponseEntity.ok(encargadoRepository.findByPropietarioNumProperty(numProperty));
    }


}
