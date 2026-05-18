package com.mgcss.api.dto;

import com.mgcss.domain.Estado;
import java.time.LocalDateTime;

public class EstadoChangeDTO {
    private Estado estadoAnterior;
    private Estado estadoNuevo;
    private LocalDateTime fechaCambio;

    public EstadoChangeDTO() {}

    public EstadoChangeDTO(Estado estadoAnterior, Estado estadoNuevo, LocalDateTime fechaCambio) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public Estado getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(Estado estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public Estado getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(Estado estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}