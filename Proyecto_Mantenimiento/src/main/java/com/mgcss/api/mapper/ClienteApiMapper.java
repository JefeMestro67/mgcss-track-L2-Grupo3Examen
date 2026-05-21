package com.mgcss.api.mapper;

import com.mgcss.api.dto.ClienteResponseDTO;
import com.mgcss.domain.Cliente;

public class ClienteApiMapper {

    private ClienteApiMapper() {
        // Constructor privado exigido por SonarCloud para evitar instanciar clases utilitarias
    }

    public static ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTipoCliente(),
            cliente.isActivo(),
            cliente.getSolicitudesAbiertas()
        );
    }
}
