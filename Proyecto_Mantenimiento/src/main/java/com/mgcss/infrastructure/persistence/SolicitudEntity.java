package com.mgcss.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mgcss.domain.Estado;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    private String descripcion;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnicoAsignado;

    private LocalDateTime fechaCierre;

    // --- NUEVO: HISTORIAL DE ESTADOS ---
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "solicitud_historial", joinColumns = @JoinColumn(name = "solicitud_id"))
    private List<EstadoChangeEntity> historial = new ArrayList<>();

    public SolicitudEntity() {
    }

    public SolicitudEntity(Long id, ClienteEntity cliente, String descripcion, LocalDateTime fechaCreacion, Estado estado, TecnicoEntity tecnicoAsignado, LocalDateTime fechaCierre) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.tecnicoAsignado = tecnicoAsignado;
        this.fechaCierre = fechaCierre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClienteEntity getCliente() { return cliente; }
    public void setCliente(ClienteEntity cliente) { this.cliente = cliente; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public TecnicoEntity getTecnicoAsignado() { return tecnicoAsignado; }
    public void setTecnicoAsignado(TecnicoEntity tecnicoAsignado) { this.tecnicoAsignado = tecnicoAsignado; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    // --- NUEVOS MÉTODOS PARA EL HISTORIAL ---
    public List<EstadoChangeEntity> getHistorial() {
        return historial;
    }

    public void setHistorial(List<EstadoChangeEntity> historial) {
        this.historial = historial;
    }
}