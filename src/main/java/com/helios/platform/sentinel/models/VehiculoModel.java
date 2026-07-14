package com.helios.platform.sentinel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehiculo")
@Data
public class VehiculoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "color")
    private String color;

    @Column(name = "placa")
    private String placa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_record_id")
    @JsonBackReference
    private OwnerRecordModel ownerRecord;
}
