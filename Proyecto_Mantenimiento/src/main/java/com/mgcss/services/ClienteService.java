package com.mgcss.services;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.TipoCliente;
import com.mgcss.infrastructure.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente crearCliente(String nombre, String email) {
        Cliente nuevoCliente = new Cliente(
            null, 
            nombre, 
            email, 
            TipoCliente.STANDARD, 
            true, 
            0
        );
        
        return clienteRepository.save(nuevoCliente);
    }
    
    public void desactivarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));
        cliente.desactivar();
        clienteRepository.save(cliente);
    }

}