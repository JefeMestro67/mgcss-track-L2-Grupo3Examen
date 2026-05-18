package com.mgcss.api.dto;

public class ClienteRequestDTO {
    
    // Quitamos 'final' para que Jackson pueda rellenar los campos tras instanciar
    private String nombre;
    private String email;

    // CONSTRUCTOR VACÍO OBLIGATORIO: Exigido por Spring/Jackson para la deserialización del JSON
    public ClienteRequestDTO() {
    }

    // Constructor completo (el que ya usáis en vuestro código)
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