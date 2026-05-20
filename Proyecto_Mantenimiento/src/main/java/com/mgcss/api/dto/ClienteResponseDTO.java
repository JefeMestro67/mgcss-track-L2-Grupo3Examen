package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del cliente devuelta por el sistema")
public class ClienteResponseDTO extends BaseResponseDTO {

    @Schema(description = "Dirección de correo electrónico del cliente", example = "juan.perez@mgcss.com")
    private final String email;

    @Schema(description = "Tipo o categoría de prioridad asignada al cliente dentro del dominio")
    private final TipoCliente tipoCliente;

    @Schema(description = "Número total de solicitudes que el cliente mantiene abiertas simultáneamente", example = "1")
    private final int solicitudesAbiertas;

    // Constructor vacío seguro para Jackson
    public ClienteResponseDTO() {
        super(0L, "", false);
        this.email = "";
        this.tipoCliente = null;
        this.solicitudesAbiertas = 0;
    }

    public ClienteResponseDTO(Long id, String nombre, String email, TipoCliente tipoCliente, boolean activo, int solicitudesAbiertas) {
        super(id, nombre, activo);
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public String getEmail() { return email; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }
}