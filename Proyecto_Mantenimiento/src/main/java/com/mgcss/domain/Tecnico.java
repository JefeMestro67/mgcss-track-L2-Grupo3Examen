package com.mgcss.domain;

public class Tecnico {
    
    private Long id;
    private String nombre;
    private String especialidad; 
    private boolean activo;
    private int cargaTrabajo;

    public Tecnico() {
    }
    
    public Tecnico(Long id, String nombre, String especialidad, boolean activo, int cargaTrabajo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = activo;
        this.cargaTrabajo = cargaTrabajo;
    }

    // SOLO GETTERS (Cero Setters)
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public boolean isActivo() { return activo; }
    public int getCargaTrabajo() { return cargaTrabajo; }

    // REGLAS DE NEGOCIO
    public void desactivar() {
        if (this.cargaTrabajo > 0) {
            throw new IllegalStateException("No se puede desactivar un técnico con solicitudes pendientes");
        }
        this.activo = false;
    }

    public void incrementarCarga() {
        if (!this.activo) {
            throw new IllegalStateException("No se puede asignar trabajo a un técnico inactivo");
        }
        if (this.cargaTrabajo >= 5) {
            throw new IllegalStateException("El técnico ha alcanzado su capacidad máxima de trabajo");
        }
        this.cargaTrabajo++;
    }

    public void finalizarTarea() {
        if (this.cargaTrabajo <= 0) {
            throw new IllegalStateException("El técnico no tiene tareas en curso para finalizar");
        }
        this.cargaTrabajo--;
    }
}