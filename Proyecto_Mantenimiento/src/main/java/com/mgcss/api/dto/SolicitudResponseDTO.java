package com.mgcss.api.dto;

import com.mgcss.domain.Estado;
import java.time.LocalDateTime;
import java.util.List;

public class SolicitudResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado;
    private Long tecnicoId;
    private String tecnicoNombre;
    private LocalDateTime fechaCierre;
    private List<EstadoChangeDTO> historial;

    public SolicitudResponseDTO(Long id, Long clienteId, String clienteNombre, String descripcion, 
                                LocalDateTime fechaCreacion, Estado estado, Long tecnicoId, 
                                String tecnicoNombre, LocalDateTime fechaCierre, List<EstadoChangeDTO> historial) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.tecnicoId = tecnicoId;
        this.tecnicoNombre = tecnicoNombre;
        this.fechaCierre = fechaCierre;
        this.historial = historial;
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
