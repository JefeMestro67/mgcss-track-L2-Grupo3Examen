package com.mgcss.services;

import com.mgcss.domain.Cliente;
import com.mgcss.infrastructure.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    // Inyección por constructor (Obligatorio para poder testear en aislamiento)
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void crearSolicitud(Long clienteId) {
        // 1. Protegemos la búsqueda: si el Optional está vacío, lanzamos nuestra excepción
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        // 2. El servicio llama al dominio
        cliente.crearSolicitud();

        // 3. Guardamos el estado final
        clienteRepository.save(cliente);
    }

    public void desactivarCliente(Long clienteId) {
        // 1. Recuperamos el cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        // 2. El servicio llama al dominio
        cliente.desactivar();

        // 3. Guardamos el cambio
        clienteRepository.save(cliente);
    }

    public void finalizarSolicitud(Long clienteId) {
        // 1. Recuperamos el cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        // 2. El servicio llama al dominio
        cliente.finalizarSolicitud();

        // 3. Guardamos el cambio
        clienteRepository.save(cliente);
    }

    public Cliente crearCliente(String nombre, String email) {
        // Creamos un cliente puro de dominio, sin ID (lo genera la BD) y activo por defecto
        Cliente nuevoCliente = new Cliente(null, nombre, email, true, 0);
        
        // Delegamos en el repositorio (el puerto) para que lo guarde
        return clienteRepository.save(nuevoCliente);
    }
}
