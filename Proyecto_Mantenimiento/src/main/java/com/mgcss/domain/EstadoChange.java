package com.mgcss.domain;

import java.time.LocalDateTime;

public class EstadoChange {
    private final Estado estadoAnterior;
    private final Estado estadoNuevo;
    private final LocalDateTime fechaCambio;

    public EstadoChange(Estado estadoAnterior, Estado estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = LocalDateTime.now();
    }

    public EstadoChange(Estado estadoAnterior, Estado estadoNuevo, LocalDateTime fechaCambio) {
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