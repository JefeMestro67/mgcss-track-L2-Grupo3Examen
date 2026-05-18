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

    public SolicitudResponseDTO() {}

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

    // GETTERS Y SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

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
