package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;

public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private TipoCliente tipoCliente;
    private boolean activo;
    private int solicitudesAbiertas;

    public ClienteResponseDTO(Long id, String nombre, String email, TipoCliente tipoCliente, boolean activo, int solicitudesAbiertas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.activo = activo;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getSolicitudesAbiertas() {
        return solicitudesAbiertas;
    }
}