package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos requeridos para registrar un nuevo cliente en el sistema")
public class ClienteRequestDTO {
    
    @Schema(description = "Nombre completo del cliente", example = "Juan Perez")
    private String nombre;

    @Schema(description = "Dirección de correo electrónico de contacto", example = "juan.perez@mgcss.com")
    private String email;

    public ClienteRequestDTO() {
    }

    public ClienteRequestDTO(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) {
        this.email = email;
    }
}