package com.mgcss.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnicos")
public class TecnicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private boolean activo;

    private int cargaTrabajo;

    public TecnicoEntity() {
    }

    public TecnicoEntity(Long id, String nombre, boolean activo, int cargaTrabajo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.cargaTrabajo = cargaTrabajo;
    }

    // Getters y Setters necesarios para el RepositoryAdapter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getCargaTrabajo() { return cargaTrabajo; }
    public void setCargaTrabajo(int cargaTrabajo) { this.cargaTrabajo = cargaTrabajo; }
}
