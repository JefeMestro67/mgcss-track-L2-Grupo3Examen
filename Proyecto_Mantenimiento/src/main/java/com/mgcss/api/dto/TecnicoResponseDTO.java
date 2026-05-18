package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del operario técnico devuelta por el sistema")
public class TecnicoResponseDTO extends BaseResponseDTO {

    @Schema(description = "Área de especialización técnica del operario", example = "Sistemas de Redes")
    private String especialidad;

    @Schema(description = "Número de incidencias o tareas activas asignadas actualmente al técnico", example = "2")
    private int cargaTrabajo;

    public TecnicoResponseDTO(
            Long id,
            String nombre,
            String especialidad,
            boolean activo,
            int cargaTrabajo
    ) {
        super(id, nombre, activo);
        this.especialidad = especialidad;
        this.cargaTrabajo = cargaTrabajo;
    }

    @Override
    @Schema(description = "Identificador único del técnico generado por la base de datos", example = "1")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Schema(description = "Nombre completo del técnico", example = "Carlos Gomez")
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    @Schema(description = "Estado de disponibilidad del técnico para recibir nuevas tareas", example = "true")
    public boolean isActivo() {
        return super.isActivo();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public int getCargaTrabajo() {
        return cargaTrabajo;
    }
}
