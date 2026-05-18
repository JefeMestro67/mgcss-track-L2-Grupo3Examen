package com.mgcss.api.dto;

public class TecnicoRequestDTO {
    private String nombre;
    private String especialidad;

    public TecnicoRequestDTO() {
    }

    public TecnicoRequestDTO(String nombre, String especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // GETTERS Y SETTERS STANDARD (Requeridos por Jackson para serializar/deserializar)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}