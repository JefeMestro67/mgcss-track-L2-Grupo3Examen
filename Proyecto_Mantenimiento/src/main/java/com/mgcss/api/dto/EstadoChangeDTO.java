package com.mgcss.api.dto;

import com.mgcss.domain.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Registro detallado de un cambio de estado en el ciclo de vida de la solicitud")
public class EstadoChangeDTO {

    @Schema(description = "Estado en el que se encontraba la solicitud antes de la modificación")
    private Estado estadoAnterior;

    @Schema(description = "Nuevo estado asignado a la solicitud tras completarse la operación")
    private Estado estadoNuevo;

    @Schema(description = "Fecha y hora exacta en la que se procesó el cambio de estado")
    private LocalDateTime fechaCambio;

    public EstadoChangeDTO(Estado estadoAnterior, Estado estadoNuevo, LocalDateTime fechaCambio) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public Estado getEstadoAnterior() {
        return estadoAnterior;
    }

    public Estado getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }
}