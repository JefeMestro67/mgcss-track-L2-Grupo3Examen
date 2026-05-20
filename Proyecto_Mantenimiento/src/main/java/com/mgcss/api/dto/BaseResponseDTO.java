package com.mgcss.api.dto;

public abstract class BaseResponseDTO {

    protected Long id;
    protected String nombre;
    protected boolean activo;

    // SonarCloud: Visibilidad cambiada a protected ya que la clase es abstracta
    protected BaseResponseDTO(Long id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }
}