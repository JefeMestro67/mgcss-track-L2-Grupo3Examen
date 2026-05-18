package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos requeridos para registrar un nuevo operario técnico en el sistema")
public class TecnicoRequestDTO {

    @Schema(description = "Nombre completo del técnico", example = "Carlos Gomez")
    private String nombre;

    @Schema(description = "Área de especialización técnica del operario", example = "Sistemas de Redes")
    private String especialidad;

    public TecnicoRequestDTO() {
    }

    public TecnicoRequestDTO(String nombre, String especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

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