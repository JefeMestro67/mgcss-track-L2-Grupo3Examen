package com.mgcss.api.dto;

import com.mgcss.domain.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Información detallada de la solicitud devuelta por el sistema")
public class SolicitudResponseDTO {

    @Schema(description = "Identificador único de la solicitud generado por la base de datos", example = "1")
    private Long id;

    @Schema(description = "ID del cliente asociado a la solicitud", example = "1")
    private Long clienteId;

    @Schema(description = "Nombre completo del cliente", example = "Juan Perez")
    private String clienteNombre;

    @Schema(description = "Texto explicativo con el detalle del problema técnico", example = "El servidor de base de datos no responde")
    private String descripcion;

    @Schema(description = "Fecha y hora exacta en la que se registró la incidencia")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Estado actual del ciclo de vida de la solicitud")
    private Estado estado;

    @Schema(description = "ID del técnico asignado para resolver la incidencia", example = "2")
    private Long tecnicoId;

    @Schema(description = "Nombre completo del técnico asignado", example = "Carlos Gomez")
    private String tecnicoNombre;

    @Schema(description = "Fecha y hora en la que se cerró la solicitud si procede")
    private LocalDateTime fechaCierre;

    @Schema(description = "Historial completo con todas las transiciones de estado sufridas por la solicitud")
    private List<EstadoChangeDTO> historial;


    public SolicitudResponseDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getMainDescripcion() { return descripcion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public List<EstadoChangeDTO> getHistorial() { return historial; }
    public void setHistorial(List<EstadoChangeDTO> historial) { this.historial = historial; }
}