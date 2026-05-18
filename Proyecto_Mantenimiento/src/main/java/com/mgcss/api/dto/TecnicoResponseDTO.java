package com.mgcss.api.dto;

public class TecnicoResponseDTO {
    private Long id;
    private String nombre;
    private String especialidad;
    private boolean activo;
    private int cargaTrabajo;

    public TecnicoResponseDTO(Long id, String nombre, String especialidad, boolean activo, int cargaTrabajo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = activo;
        this.cargaTrabajo = cargaTrabajo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getCargaTrabajo() {
        return cargaTrabajo;
    }
}
