package com.helios.platform.sentinel.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cac_access_data", indexes = {
    @Index(name = "idx_fecha_acceso", columnList = "fecha_acceso")
})
public class OwnerRecordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Override
    public String toString() {
        return "OwnerRecordModel{" +
                "id=" + id +
                ", fecha_acceso='" + fecha_acceso + '\'' +
                ", propertyID='" + propertyID + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", type='" + type + '\'' +
                ", qtyPeople=" + qtyPeople +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", cedula='" + cedula + '\'' +
                ", acompanantes=" + acompanantes +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFecha_acceso() {
        return fecha_acceso;
    }

    public void setFecha_acceso(String fecha_acceso) {
        this.fecha_acceso = fecha_acceso;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQtyPeople() {
        return qtyPeople;
    }

    public void setQtyPeople(Integer qtyPeople) {
        this.qtyPeople = qtyPeople;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Integer getAcompanantes() {
        return acompanantes;
    }

    public void setAcompanantes(Integer acompanantes) {
        this.acompanantes = acompanantes;
    }

    @Column(columnDefinition = "VARCHAR(255)")
    private String fecha_acceso;

    @Column(name = "propertyID", columnDefinition = "VARCHAR(255)")
    private String propertyID;

    @Column(name = "buildingName", columnDefinition = "VARCHAR(255)")
    private String buildingName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String type;

    @Column(name = "qtyPeople", columnDefinition = "INT")
    private Integer qtyPeople;

    @Column(columnDefinition = "VARCHAR(255)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(255)")
    private String apellido;

    @Column(columnDefinition = "VARCHAR(255)")
    private String cedula;

    @Column(name = "acompanantes", columnDefinition = "INT")
    private Integer acompanantes;

    @OneToMany(mappedBy = "ownerRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.BatchSize(size = 500)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private java.util.List<VehiculoModel> vehiculos;

    public void setVehiculos(java.util.List<VehiculoModel> vehiculos) {
        this.vehiculos = vehiculos;
        if (vehiculos != null) {
            for (VehiculoModel v : vehiculos) {
                v.setOwnerRecord(this);
            }
        }
    }

    public java.util.List<VehiculoModel> getVehiculos() {
        return vehiculos;
    }

}
