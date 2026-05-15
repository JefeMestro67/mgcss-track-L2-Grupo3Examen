package com.mgcss.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitudRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    public SolicitudRequestDTO() {
    }

    public SolicitudRequestDTO(Long clienteId, String descripcion) {
        this.clienteId = clienteId;
        this.descripcion = descripcion;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "SolicitudRequestDTO{" +
                "clienteId=" + clienteId +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
