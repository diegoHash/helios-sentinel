package com.helios.platform.sentinel.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carga_familiar")
@Data
public class CargaFamiliarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inmueble")
    private String inmueble;

    @Column(name = "nombre_propietario")
    private String nombrePropietario;

    @Column(name = "cedula_propietario")
    private String cedulaPropietario;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    // Estos campos aplican para la carga familiar o el encargado
    @Column(name = "nombre_persona")
    private String nombrePersona;

    @Column(name = "cedula_persona")
    private String cedulaPersona;

    @Column(name = "parentesco")
    private String parentesco;

    // 'CARGA_FAMILIAR' o 'ENCARGADO'
    @Column(name = "tipo_registro")
    private String tipoRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private PropietarioModel propietario;
}
