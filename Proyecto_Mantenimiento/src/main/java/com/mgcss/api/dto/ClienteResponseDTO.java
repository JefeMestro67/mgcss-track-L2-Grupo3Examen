package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;

public class ClienteResponseDTO extends BaseResponseDTO {

    private String email;
    private TipoCliente tipoCliente;
    private int solicitudesAbiertas;

    public ClienteResponseDTO(
            Long id,
            String nombre,
            String email,
            TipoCliente tipoCliente,
            boolean activo,
            int solicitudesAbiertas
    ) {
        super(id, nombre, activo);
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public String getEmail() {
        return email;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public int getSolicitudesAbiertas() {
        return solicitudesAbiertas;
    }
}