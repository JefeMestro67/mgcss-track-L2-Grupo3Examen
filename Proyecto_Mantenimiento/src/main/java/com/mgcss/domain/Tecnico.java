package com.mgcss.domain;

public class Tecnico {
    
    private Long id;
    private String nombre;
    private boolean activo;
    private int cargaTrabajo; // Número de solicitudes asignadas actualmente

    public Tecnico() {
    }
    public Tecnico(boolean activo) {
        this.activo = activo;
        this.cargaTrabajo = 0; // Valor por defecto coherente
    }
    public Tecnico(Long id, String nombre, boolean activo, int cargaTrabajo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.cargaTrabajo = cargaTrabajo;
    }
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public int getCargaTrabajo() { return cargaTrabajo; }
    public void setCargaTrabajo(int cargaTrabajo) { this.cargaTrabajo = cargaTrabajo; }


    // Regla 1: Un técnico no puede ser desactivado si tiene trabajo pendiente
    public void desactivar() {
        if (this.cargaTrabajo > 0) {
            throw new IllegalStateException("No se puede desactivar un técnico con solicitudes pendientes");
        }
        this.activo = false;
    }

    // Regla 2: Controlar el límite de carga (ej. máximo 5 tareas)
    public void incrementarCarga() {
        if (!this.activo) {
            throw new IllegalStateException("No se puede asignar trabajo a un técnico inactivo");
        }
        if (this.cargaTrabajo >= 5) {
            throw new IllegalStateException("El técnico ha alcanzado su capacidad máxima de trabajo");
        }
        this.cargaTrabajo++;
    }

    // Regla 3: Finalización de tarea
    public void finalizarTarea() {
        if (this.cargaTrabajo <= 0) {
            throw new IllegalStateException("El técnico no tiene tareas en curso para finalizar");
        }
        this.cargaTrabajo--;
    }
}
