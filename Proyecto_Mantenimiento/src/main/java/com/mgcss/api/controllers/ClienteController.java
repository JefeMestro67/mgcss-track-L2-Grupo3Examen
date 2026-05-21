package com.mgcss.api.controllers;

import com.mgcss.api.dto.ClienteRequestDTO;
import com.mgcss.api.dto.ClienteResponseDTO;
import com.mgcss.api.mapper.ClienteApiMapper;
import com.mgcss.domain.Cliente;
import com.mgcss.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Controlador para el registro de clientes y la gestión de su estado en el sistema")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente", description = "Crea un cliente de manera persistentente en el sistema utilizando su nombre y dirección de correo electrónico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente registrado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o formato de email inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO request) { // Arquitectura: @Valid integrado
        Cliente nuevo = clienteService.crearCliente(request.getNombre(), request.getEmail());
        return new ResponseEntity<>(ClienteApiMapper.toResponseDTO(nuevo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar un cliente", description = "Cambia el estado del cliente a inactivo, lo cual restringe ciertas operaciones comerciales dentro del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente desactivado con éxito"),
        @ApiResponse(responseCode = "404", description = "El cliente con el ID especificado no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor o incumplimiento de regla de negocio")
    })
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID único del cliente a dar de baja", example = "1") @PathVariable Long id) {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }

}