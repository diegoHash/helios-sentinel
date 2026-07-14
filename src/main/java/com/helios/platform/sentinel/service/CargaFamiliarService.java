package com.helios.platform.sentinel.service;

import com.helios.platform.sentinel.models.CargaFamiliarModel;
import com.helios.platform.sentinel.models.EncargadoModel;
import com.helios.platform.sentinel.models.PropietarioModel;

import com.helios.platform.sentinel.repository.CargaFamiliarRepository;
import com.helios.platform.sentinel.repository.EncargadoRepository;
import com.helios.platform.sentinel.repository.ISentinelOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CargaFamiliarService {

    @Autowired
    private CargaFamiliarRepository cargaFamiliarRepository;

    @Autowired
    private EncargadoRepository encargadoRepository;

    @Autowired
    private ISentinelOwnerRepository propietarioRepository;

    public List<CargaFamiliarModel> obtenerTodos() {
        return cargaFamiliarRepository.findAll();
    }


}
