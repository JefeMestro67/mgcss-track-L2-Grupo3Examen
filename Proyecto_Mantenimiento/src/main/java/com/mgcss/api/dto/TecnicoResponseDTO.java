package com.mgcss.api.dto;

public class TecnicoResponseDTO extends BaseResponseDTO {

    private String especialidad;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public int getCargaTrabajo() {
        return cargaTrabajo;
    }
}
