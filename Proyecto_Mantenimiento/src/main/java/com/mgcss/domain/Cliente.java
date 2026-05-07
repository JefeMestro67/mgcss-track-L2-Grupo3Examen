package com.mgcss.domain;

public class Cliente {
    
    private Long id;
    private String nombre;
    private String email;
    private TipoCliente tipoCliente; 
    private boolean activo;
    private int solicitudesAbiertas;

    public Cliente() {
        this.tipoCliente = TipoCliente.STANDARD; 
    }

    public Cliente(Long id, String nombre, String email, TipoCliente tipoCliente, boolean activo, int solicitudesAbiertas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.activo = activo;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    // SOLO GETTERS (Cero Setters)
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public boolean isActivo() { return activo; }
    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }

    // REGLAS DE NEGOCIO
    public void desactivar() {
        if (this.solicitudesAbiertas > 0) {
            throw new IllegalStateException("No se puede desactivar un cliente con solicitudes abiertas");
        }
        this.activo = false;
    }

    public void crearSolicitud() {
        if (!this.activo) {
            throw new IllegalStateException("Solo un cliente activo puede crear nuevas solicitudes");
        }
        if (this.solicitudesAbiertas >= 3) {
            throw new IllegalStateException("El cliente ha alcanzado su límite de solicitudes simultáneas permitidas");
        }
        this.solicitudesAbiertas++;
    }

    public void finalizarSolicitud() {
        if (this.solicitudesAbiertas <= 0) {
            throw new IllegalStateException("El cliente no tiene solicitudes abiertas para finalizar");
        }
        this.solicitudesAbiertas--;
    }
}