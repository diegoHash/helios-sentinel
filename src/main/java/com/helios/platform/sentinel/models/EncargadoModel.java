package com.helios.platform.sentinel.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "encargado")
@Data
public class EncargadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inmueble")
    private String inmueble;

    @Column(name = "nombre_persona")
    private String nombrePersona;

    @Column(name = "cedula_persona")
    private String cedulaPersona;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private PropietarioModel propietario;
}
