package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;

public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private TipoCliente tipoCliente;
    private boolean activo;
    private int solicitudesAbiertas;

    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(Long id, String nombre, String email, TipoCliente tipoCliente, boolean activo, int solicitudesAbiertas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.activo = activo;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }

    public boolean isActive() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }
    public void setSolicitudesAbiertas(int solicitudesAbiertas) { this.solicitudesAbiertas = solicitudesAbiertas; }
}