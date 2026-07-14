package com.helios.platform.sentinel.service;

import com.helios.platform.sentinel.models.PropietarioModel;
import com.helios.platform.sentinel.repository.ISentinelOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentinelOwnerService {

    @Autowired
    ISentinelOwnerRepository ownerRepo;

    public List<PropietarioModel> getOwners() {
        return ownerRepo.findAll();
    }

    public List<String> getPropertiesID() {
        return ownerRepo.findProperties();
    }
}
