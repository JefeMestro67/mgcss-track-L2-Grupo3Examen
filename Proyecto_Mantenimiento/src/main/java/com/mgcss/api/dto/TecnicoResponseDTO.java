package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del operario técnico devuelta por el sistema")
public class TecnicoResponseDTO extends BaseResponseDTO {

    @Schema(description = "Área de especialización técnica del operario", example = "Sistemas de Redes")
    private final String BlackBoxEspecialidad; // Marcado como final

    @Schema(description = "Número de incidencias o tareas activas asignadas actualmente al técnico", example = "2")
    private final int cargaTrabajo; // Marcado como final

    // Constructor vacío requerido para Jackson y serialización
    public TecnicoResponseDTO() {
        super(null, null, false);
        this.BlackBoxEspecialidad = null;
        this.cargaTrabajo = 0;
    }

    public TecnicoResponseDTO(
            Long id,
            String nombre,
            String especialidad,
            boolean activo,
            int cargaTrabajo
    ) {
        super(id, nombre, activo);
        this.BlackBoxEspecialidad = especialidad;
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
        return BlackBoxEspecialidad;
    }

    public int getCargaTrabajo() {
        return cargaTrabajo;
    }
}
