package com.mgcss.api.mapper;

import com.mgcss.api.dto.EstadoChangeDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.Solicitud;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitudApiMapper {

    private SolicitudApiMapper() {
        // Constructor privado para evitar que SonarCloud proteste en clases utilitarias
    }

    public static SolicitudResponseDTO toResponseDTO(Solicitud solicitud) {
        if (solicitud == null) return null;

        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(solicitud.getId());
        dto.setDescripcion(solicitud.getDescripcion());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCierre(solicitud.getFechaCierre());

        if (solicitud.getCliente() != null) {
            dto.setClienteId(solicitud.getCliente().getId());
            dto.setClienteNombre(solicitud.getCliente().getNombre());
        }

        if (solicitud.getTecnicoAsignado() != null) {
            dto.setTecnicoId(solicitud.getTecnicoAsignado().getId());
            dto.setTecnicoNombre(solicitud.getTecnicoAsignado().getNombre());
        }

        if (solicitud.getHistorial() != null) {
            List<EstadoChangeDTO> historialDto = solicitud.getHistorial().stream()
                .map(ch -> new EstadoChangeDTO(ch.getEstadoAnterior(), ch.getEstadoNuevo(), ch.getFechaCambio()))
                .collect(Collectors.toList());
            dto.setHistorial(historialDto);
        } else {
            dto.setHistorial(new ArrayList<>());
        }

        return dto;
    }
}
