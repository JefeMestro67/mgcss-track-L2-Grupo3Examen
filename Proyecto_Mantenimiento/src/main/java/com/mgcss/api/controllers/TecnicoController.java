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

    @PutMapping("/{id}/asignar-tarea")
    @Operation(summary = "Incrementar carga de trabajo", description = "Incrementa manualmente el contador de tareas o solicitudes activas asignadas a este técnico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carga incrementada con éxito"),
        @ApiResponse(responseCode = "400", description = "Operación rechazada (ej. el técnico superaría el límite máximo de carga permitido)"),
        @ApiResponse(responseCode = "404", description = "El técnico con el ID proporcionado no existe")
    })
    public ResponseEntity<Void> asignarTarea(
            @Parameter(description = "ID del técnico al que se le asigna la tarea", example = "1") @PathVariable Long id) {
        tecnicoService.asignarNuevaTarea(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/finalizar-tarea")
    @Operation(summary = "Finalizar tarea", description = "Reduce la carga de trabajo del técnico al marcar una de sus tareas activas como completada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carga de trabajo reducida con éxito"),
        @ApiResponse(responseCode = "400", description = "Operación inválida (ej. el técnico ya tiene 0 tareas asignadas y no puede reducirse más)"),
        @ApiResponse(responseCode = "404", description = "El técnico con el ID proporcionado no existe")
    })
    public ResponseEntity<Void> finalizarTarea(
            @Parameter(description = "ID del técnico que finaliza la tarea", example = "1") @PathVariable Long id) {
        tecnicoService.finalizarTarea(id);
        return ResponseEntity.noContent().build();
    }
}