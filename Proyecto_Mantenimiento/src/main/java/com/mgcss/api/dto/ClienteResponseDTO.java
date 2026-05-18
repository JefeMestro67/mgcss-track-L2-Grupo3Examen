package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del cliente devuelta por el sistema")
public class ClienteResponseDTO extends BaseResponseDTO {

    @Schema(description = "Dirección de correo electrónico del cliente", example = "juan.perez@mgcss.com")
    private String email;

    @Schema(description = "Tipo o categoría de prioridad asignada al cliente dentro del dominio")
    private TipoCliente tipoCliente;

    @Schema(description = "Número total de solicitudes que el cliente mantiene abiertas simultáneamente", example = "1")
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

    @Override
    @Schema(description = "Identificador único del cliente generado por la base de datos", example = "1")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Schema(description = "Nombre completo del cliente registrado", example = "Juan Perez")
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    @Schema(description = "Estado de habilitación del cliente en el sistema", example = "true")
    public boolean isActivo() {
        return super.isActivo();
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