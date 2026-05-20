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
@Tag(name = "Técnicos", description = "Controlador restringido para la gestión del estado administrativo de los operarios técnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar un técnico", description = "Cambia el estado del técnico a inactivo. La operación fallará con un error de negocio si el técnico tiene tareas pendientes asignadas actualmente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Técnico desactivado con éxito"),
        @ApiResponse(responseCode = "400", description = "Regla de negocio violada (el técnico posee cargas de trabajo activas sin resolver)"),
        @ApiResponse(responseCode = "404", description = "El técnico con el ID proporcionado no existe")
    })
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID único del técnico a desactivar", example = "1") @PathVariable Long id) {
        tecnicoService.desactivarTecnico(id);
        return ResponseEntity.noContent().build();
    }
}