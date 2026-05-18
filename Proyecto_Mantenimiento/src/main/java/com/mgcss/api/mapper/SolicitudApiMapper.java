package com.mgcss.api.mapper;

import com.mgcss.api.dto.EstadoChangeDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.Solicitud;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitudApiMapper {

    private SolicitudApiMapper() {
    }

    public static SolicitudResponseDTO toResponseDTO(Solicitud solicitud) {
        if (solicitud == null) return null;

        // 1. Extraer datos del cliente si existe
        Long clienteId = null;
        String clienteNombre = null;
        if (solicitud.getCliente() != null) {
            clienteId = solicitud.getCliente().getId();
            clienteNombre = solicitud.getCliente().getNombre();
        }

        // 2. Extraer datos del técnico si existe
        Long tecnicoId = null;
        String tecnicoNombre = null;
        if (solicitud.getTecnicoAsignado() != null) {
            tecnicoId = solicitud.getTecnicoAsignado().getId();
            tecnicoNombre = solicitud.getTecnicoAsignado().getNombre();
        }

        // 3. Mapear el historial de estados
        List<EstadoChangeDTO> historialDto;
        if (solicitud.getHistorial() != null) {
            historialDto = solicitud.getHistorial().stream()
                .map(ch -> new EstadoChangeDTO(ch.getEstadoAnterior(), ch.getEstadoNuevo(), ch.getFechaCambio()))
                .collect(Collectors.toList());
        } else {
            historialDto = new ArrayList<>();
        }

        // 4. Construimos el DTO inmutable de un solo golpe usando su constructor
        return new SolicitudResponseDTO(
            solicitud.getId(),
            clienteId,
            clienteNombre,
            solicitud.getDescripcion(),
            solicitud.getFechaCreacion(),
            solicitud.getEstado(),
            tecnicoId,
            tecnicoNombre,
            solicitud.getFechaCierre(),
            historialDto
        );
    }
}
