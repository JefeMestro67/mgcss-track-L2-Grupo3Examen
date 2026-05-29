package com.mgcss.api.controllers;

import com.mgcss.api.dto.TecnicoRequestDTO;
import com.mgcss.api.dto.TecnicoResponseDTO;
import com.mgcss.api.mapper.TecnicoApiMapper;
import com.mgcss.domain.Tecnico;
import com.mgcss.services.TecnicoService;
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
        // Mapea la infracción de negocio (Ej: desactivar operario con tareas activas) interceptada por el Handler
        @ApiResponse(responseCode = "400", description = "Violación de regla de negocio: El técnico posee carga de trabajo pendiente"),
        // Mapea fallos técnicos inesperados de infraestructura
        @ApiResponse(responseCode = "500", description = "Error interno del servidor ante un fallo técnico imprevisto")
    })
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID único del técnico a desactivar", example = "1") @PathVariable Long id) {
        tecnicoService.desactivarTecnico(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping
    @Operation(summary = "Registrar un nuevo técnico", description = "Crea un operario técnico en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Técnico registrado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o DTO inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TecnicoResponseDTO> crear(@Valid @RequestBody TecnicoRequestDTO request) {
        Tecnico nuevo = tecnicoService.crearTecnico(request.getNombre(), request.getEspecialidad());
        return new ResponseEntity<>(TecnicoApiMapper.toResponseDTO(nuevo), HttpStatus.CREATED);
    }

}