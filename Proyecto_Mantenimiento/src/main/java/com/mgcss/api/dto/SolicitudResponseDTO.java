package com.mgcss.api.dto;

public class SolicitudResponseDTO {
    private Long id;
    private String descripcion;
    private String estado;

    public SolicitudResponseDTO(Long id, String descripcion, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }
}
