package com.mgcss.infrastructure.persistence;

import jakarta.persistence.Entity;
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

    private boolean activo;

    private int solicitudesAbiertas;

    public ClienteEntity() {
    }

    public ClienteEntity(Long id, String nombre, String email, boolean activo, int solicitudesAbiertas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getSolicitudesAbiertas() { return solicitudesAbiertas; }
    public void setSolicitudesAbiertas(int solicitudesAbiertas) { this.solicitudesAbiertas = solicitudesAbiertas; }
}
