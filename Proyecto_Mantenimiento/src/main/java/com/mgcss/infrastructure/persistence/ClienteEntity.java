package com.mgcss.infrastructure.persistence;

import com.mgcss.domain.TipoCliente;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    private boolean activo;

    private int solicitudesAbiertas;

    public ClienteEntity() {
    }

    public ClienteEntity(Long id, String nombre, String email, TipoCliente tipoCliente, boolean activo, int solicitudesAbiertas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.activo = activo;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }
    public void setSolicitudesAbiertas(int solicitudesAbiertas) { this.solicitudesAbiertas = solicitudesAbiertas; }
}
