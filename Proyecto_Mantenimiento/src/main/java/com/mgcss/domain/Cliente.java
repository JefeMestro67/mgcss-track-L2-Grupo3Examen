package com.mgcss.domain;

public class Cliente {
    
    private Long id;
    private String nombre;
    private String email;
    private boolean activo;
    private int solicitudesAbiertas; // Número de solicitudes que el cliente tiene pendientes

    public Cliente() {
    }

    public Cliente(boolean activo) {
        this.activo = activo;
        this.solicitudesAbiertas = 0; // Valor por defecto coherente
    }

    public Cliente(Long id, String nombre, String email, boolean activo, int solicitudesAbiertas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }
    public void setSolicitudesAbiertas(int solicitudesAbiertas) { this.solicitudesAbiertas = solicitudesAbiertas; }

    // Regla 1: Un cliente no puede ser desactivado si tiene solicitudes pendientes
    public void desactivar() {
        if (this.solicitudesAbiertas > 0) {
            throw new IllegalStateException("No se puede desactivar un cliente con solicitudes abiertas");
        }
        this.activo = false;
    }

    // Regla 2: Un cliente inactivo no puede crear una nueva solicitud y hay límite de solicitudes
    public void crearSolicitud() {
        if (!this.activo) {
            throw new IllegalStateException("Solo un cliente activo puede crear nuevas solicitudes");
        }
        if (this.solicitudesAbiertas >= 3) {
            throw new IllegalStateException("El cliente ha alcanzado su límite de solicitudes simultáneas permitidas");
        }
        this.solicitudesAbiertas++;
    }

    // Regla 3: Se completa/cierra una solicitud, disminuyendo la carga del cliente
    public void finalizarSolicitud() {
        if (this.solicitudesAbiertas <= 0) {
            throw new IllegalStateException("El cliente no tiene solicitudes abiertas para finalizar");
        }
        this.solicitudesAbiertas--;
    }
}
