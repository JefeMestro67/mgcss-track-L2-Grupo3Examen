package com.mgcss.api.dto;

import java.time.LocalDateTime;

public class SolicitudResponseDTO {

    private Long id;
    private String descripcion;
    private String estado;
    private String nombreCliente;
    private String nombreTecnico;
    private LocalDateTime fechaCreacion;
    private int totalCambiosEstado;

    public SolicitudResponseDTO() {
    }

    public SolicitudResponseDTO(Long id, String descripcion, String estado, 
                                String nombreCliente, String nombreTecnico, 
                                LocalDateTime fechaCreacion, int totalCambiosEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
        this.nombreTecnico = nombreTecnico;
        this.fechaCreacion = fechaCreacion;
        this.totalCambiosEstado = totalCambiosEstado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getTotalCambiosEstado() {
        return totalCambiosEstado;
    }

    public void setTotalCambiosEstado(int totalCambiosEstado) {
        this.totalCambiosEstado = totalCambiosEstado;
    }

    @Override
    public String toString() {
        return "SolicitudResponseDTO{" +
                "id=" + id +
                ", estado='" + estado + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                '}';
    }
}
