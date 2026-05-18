package com.mgcss.api.controllers;

import com.mgcss.api.dto.ClienteRequestDTO;
import com.mgcss.api.dto.ClienteResponseDTO;
import com.mgcss.api.mapper.ClienteApiMapper;
import com.mgcss.domain.Cliente;
import com.mgcss.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Paso 2.2: POST → Crear un cliente
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@RequestBody ClienteRequestDTO request) {
        Cliente nuevo = clienteService.crearCliente(request.getNombre(), request.getEmail());
        return new ResponseEntity<>(ClienteApiMapper.toResponseDTO(nuevo), HttpStatus.CREATED);
    }

    // Paso 2.2: PUT → Desactivar un cliente
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }

    // Paso 2.2: PUT → Finalizar una solicitud (Decrementar el contador de solicitudes abiertas)
    @PutMapping("/{id}/finalizar-solicitud")
    public ResponseEntity<Void> finalizarSolicitud(@PathVariable Long id) {
        clienteService.finalizarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
