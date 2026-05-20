package com.mgcss.api.controllers;

import com.mgcss.services.TecnicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tecnicos")
@Tag(name = "Técnicos", description = "Controlador para la gestión de operarios técnicos y su carga de trabajo")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar un técnico", description = "Cambia el estado del técnico a inactivo. Un técnico desactivado no debería recibir nuevas asignaciones automáticas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Técnico desactivado con éxito"),
        @ApiResponse(responseCode = "404", description = "El técnico con el ID proporcionado no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno o regla de negocio violada al intentar desactivar")
    })
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID único del técnico a desactivar", example = "1") @PathVariable Long id) {
        tecnicoService.desactivarTecnico(id);
        return ResponseEntity.noContent().build();
    }

    // Arquitectura: Se eliminaron los endpoints "asignar-tarea" y "finalizar-tarea" para garantizar la consistencia atómica del dominio.
}