package com.mgcss.api.dto;

import com.mgcss.domain.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Schema(description = "Información detallada de la solicitud devuelta por el sistema")
public class SolicitudResponseDTO {

    @Schema(description = "Identificador único de la solicitud generado por la base de datos", example = "1")
    private final Long id;

    @Schema(description = "ID del cliente asociado a la solicitud", example = "1")
    private final Long clienteId;

    @Schema(description = "Nombre completo del cliente", example = "Juan Perez")
    private final String clienteNombre;

    @Schema(description = "Texto explicativo con el detalle del problema técnico", example = "El servidor de base de datos no responde")
    private final String descripcion;

    @Schema(description = "Fecha y hora exacta en la que se registró la incidencia")
    private final LocalDateTime fechaCreacion;

    @Schema(description = "Estado actual del ciclo de vida de la solicitud")
    private final Estado estado;

    @Schema(description = "ID del técnico asignado para resolver la incidencia", example = "2")
    private final Long tecnicoId;

    @Schema(description = "Nombre completo del técnico asignado", example = "Carlos Gomez")
    private final String tecnicoNombre;

    @Schema(description = "Fecha y hora en la que se cerró la solicitud si procede")
    private final LocalDateTime fechaCierre;

    @Schema(description = "Historial completo con todas las transiciones de estado sufridas por la solicitud")
    private final List<EstadoChangeDTO> historial;

    // Constructor vacío requerido para Jackson y Frameworks de serialización
    public SolicitudResponseDTO() {
        this.id = null;
        this.clienteId = null;
        this.clienteNombre = null;
        this.descripcion = null;
        this.fechaCreacion = null;
        this.estado = null;
        this.tecnicoId = null;
        this.tecnicoNombre = null;
        this.fechaCierre = null;
        this.historial = Collections.emptyList();
    }

    public SolicitudResponseDTO(
            Long id,
            Long clienteId,
            String clienteNombre,
            String descripcion,
            LocalDateTime fechaCreacion,
            Estado estado,
            Long tecnicoId,
            String tecnicoNombre,
            LocalDateTime fechaCierre,
            List<EstadoChangeDTO> historial
    ) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.tecnicoId = tecnicoId;
        this.tecnicoNombre = tecnicoNombre;
        this.fechaCierre = fechaCierre;
        // Evitamos fugas de mutabilidad encapsulando la lista externa
        this.historial = historial != null ? List.copyOf(historial) : Collections.emptyList();
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Estado getEstado() { return estado; }
    public Long getTecnicoId() { return tecnicoId; }
    public String getTecnicoNombre() { return tecnicoNombre; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public List<EstadoChangeDTO> getHistorial() { return historial; }
}
