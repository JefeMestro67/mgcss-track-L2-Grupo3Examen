package com.mgcss.infrastructure.persistence;

import com.mgcss.domain.Estado;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
public class EstadoChangeEntity {

    @Enumerated(EnumType.STRING)
    private Estado estadoAnterior;

    @Enumerated(EnumType.STRING)
    private Estado estadoNuevo;

    private LocalDateTime fechaCambio;

    public EstadoChangeEntity() {
    }

    public EstadoChangeEntity(Estado anterior, Estado nuevo, LocalDateTime fecha) {
        this.estadoAnterior = anterior;
        this.estadoNuevo = nuevo;
        this.fechaCambio = fecha;
    }

    public Estado getEstadoAnterior() { return estadoAnterior; }
    public Estado getEstadoNuevo() { return estadoNuevo; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
}