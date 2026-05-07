package com.mgcss.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Solicitud {
    
    private Long id;
    private Cliente cliente; 
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado; 
    private Tecnico tecnicoAsignado; 
    private LocalDateTime fechaCierre; 
    private List<EstadoChange> historial = new ArrayList<>();

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
            throw new IllegalStateException("Solo se pueden cerrar solicitudes que estén EN_PROCESO");
        }
        Estado anterior = this.estado;
        
        this.estado = Estado.CERRADA;
        this.fechaCierre = LocalDateTime.now();
        
        registrarCambio(anterior, this.estado);
    }
    
    public void asignarTecnico(Tecnico tecnico) {
        if (this.estado != Estado.ABIERTA) {
            throw new IllegalStateException("Solo se puede asignar técnico a solicitudes ABIERTAS");
        }
        if (!tecnico.isActivo()) {
            throw new IllegalStateException("No se puede asignar un técnico inactivo");
        }
        
        Estado anterior = this.estado;
        this.tecnicoAsignado = tecnico;
        this.estado = Estado.EN_PROCESO;

        registrarCambio(anterior, this.estado);
    }
    
    public void reabrir() {
        if (this.estado != Estado.CERRADA) {
            throw new IllegalStateException("Solo se pueden reabrir solicitudes que estén CERRADAS");
        }
        Estado anterior = this.estado;
        
        this.estado = Estado.EN_PROCESO;
        this.fechaCierre = null;
        
        registrarCambio(anterior, this.estado);
    }
    
    private void registrarCambio(Estado anterior, Estado nuevo) {
        this.historial.add(new EstadoChange(anterior, nuevo));
    }
    
    public List<EstadoChange> getHistorial() {
        return historial;
    }
}