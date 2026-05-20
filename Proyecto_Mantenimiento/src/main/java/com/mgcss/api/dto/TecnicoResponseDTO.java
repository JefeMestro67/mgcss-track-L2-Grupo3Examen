package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del operario técnico devuelta por el sistema")
public class TecnicoResponseDTO extends BaseResponseDTO {

    @Schema(description = "Área de especialización técnica del operario", example = "Sistemas de Redes")
    private final String especialidad;

    @Schema(description = "Número de incidencias o tareas activas asignadas actualmente al técnico", example = "2")
    private final int cargaTrabajo;

    // Constructor vacío requerido para Jackson y serialización
    public TecnicoResponseDTO() {
        super(null, null, false);
        this.especialidad = null;
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
        this.especialidad = especialidad;
        this.cargaTrabajo = cargaTrabajo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public int getCargaTrabajo() {
        return cargaTrabajo;
    }
}
