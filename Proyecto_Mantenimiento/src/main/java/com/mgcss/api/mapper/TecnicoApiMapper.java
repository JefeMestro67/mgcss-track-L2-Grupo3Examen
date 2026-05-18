package com.mgcss.api.mapper;

import com.mgcss.api.dto.TecnicoResponseDTO;
import com.mgcss.domain.Tecnico;

public class TecnicoApiMapper {

    private TecnicoApiMapper() {
        // Constructor privado para SonarCloud
    }

    public static TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        if (tecnico == null) return null;

        return new TecnicoResponseDTO(
            tecnico.getId(),
            tecnico.getNombre(),
            tecnico.getEspecialidad(),
            tecnico.isActivo(),
            tecnico.getCargaTrabajo()
        );
    }
}
