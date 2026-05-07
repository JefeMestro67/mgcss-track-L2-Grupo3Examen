package com.mgcss.domain;

import java.time.LocalDateTime;

public class Solicitud {
    
    private Long id;
    private Cliente cliente; 
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado; 
    private Tecnico tecnicoAsignado; 
    private LocalDateTime fechaCierre; 

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

    // SOLO GETTERS (Cero Setters)
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Estado getEstado() { return estado; }
    public Tecnico getTecnicoAsignado() { return tecnicoAsignado; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }

    // REGLAS DE NEGOCIO
    public void cerrar() {
        if (this.estado != Estado.EN_PROCESO) {
            throw new IllegalStateException("Solo solicitudes en proceso pueden cerrarse");
        }
        this.estado = Estado.CERRADA;
        this.fechaCierre = LocalDateTime.now(); 
    }
    
    public void asignarTecnico(Tecnico tecnico) {
        if (this.estado == Estado.CERRADA) {
            throw new IllegalStateException("No se puede asignar un técnico a una solicitud cerrada");
        }
        if (!tecnico.isActivo()) {
            throw new IllegalStateException("Solo se puede asignar un técnico activo a una solicitud");
        }
        this.estado = Estado.EN_PROCESO; 
        this.tecnicoAsignado = tecnico;
    }
    
    public void reabrir() {
        // Regla de negocio: Solo se puede reabrir si está CERRADA
        if (this.estado != Estado.CERRADA) {
            throw new IllegalStateException("Solo se pueden reabrir solicitudes que estén en estado CERRADA");
        }
        
        // Al reabrir, vuelve a estar en proceso y se limpia la fecha de finalización
        this.estado = Estado.EN_PROCESO;
        this.fechaCierre = null;
    }
}