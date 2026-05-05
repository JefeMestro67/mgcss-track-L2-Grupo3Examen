package com.mgcss.domain;

import java.time.LocalDateTime;

public class Solicitud {
    
    private Long id;
    private Cliente cliente; 
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado; 
    private Tecnico tecnicoAsignado; // Opcional, requerido por el enunciado
    private LocalDateTime fechaCierre; // Nullable

    public Solicitud() {
    }

    public Solicitud(Long id, Cliente cliente, String descripcion, LocalDateTime fechaCreacion, Estado estado, Tecnico tecnicoAsignado, LocalDateTime fechaCierre) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.tecnicoAsignado = tecnicoAsignado;
        this.fechaCierre = fechaCierre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    
    public Tecnico getTecnicoAsignado() { return tecnicoAsignado; }
    public void setTecnicoAsignado(Tecnico tecnicoAsignado) { this.tecnicoAsignado = tecnicoAsignado; }
    
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    // Regla 1: Solo se puede cerrar una solicitud si está EN_PROCESO
    public void cerrar() {
        if (this.estado != Estado.EN_PROCESO) {
            throw new IllegalStateException("Solo solicitudes en proceso pueden cerrarse");
        }
        this.estado = Estado.CERRADA;
        this.fechaCierre = LocalDateTime.now(); // Actualizamos la fecha de cierre automáticamente
    }
    
    public void asignarTecnico(Tecnico tecnico) {
        // Regla: No se puede asignar un técnico a una solicitud cerrada
        if (this.estado == Estado.CERRADA) {
            throw new IllegalStateException("No se puede asignar un técnico a una solicitud cerrada");
        }
        
        // Regla: Solo se puede asignar un técnico activo
        if (!tecnico.isActivo()) {
            throw new IllegalStateException("Solo se puede asignar un técnico activo a una solicitud");
        }

        this.estado = Estado.EN_PROCESO; 
        this.tecnicoAsignado = tecnico;
    }
}