package com.helios.platform.sentinel.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "propietario")
@Data
public class PropietarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_propietario;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "num_property", columnDefinition = "VARCHAR(45)", nullable = false)
    private String num_property;

    @Column(name = "state")
    private String state;

    @Column(name = "agreement", columnDefinition = "TINYINT")
    private Boolean agreement;

    @Column(name = "max_capacity_property", columnDefinition = "INT")
    private int max_capacity_property;

}
