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
import org.springframework.http.HttpStatus;
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

    // ENDPOINT COMPLEMENTARIO AGREGADO PARA CUMPLIR EL PASO 2.2
    @PostMapping
    @Operation(summary = "Registrar un nuevo operario técnico", description = "Crea un operario de manera persistente en el sistema utilizando su nombre y área de especialización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Técnico registrado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o faltantes")
    })
    public ResponseEntity<TecnicoResponseDTO> crear(@RequestBody TecnicoRequestDTO request) {
        // 1. Validar y Delegar al servicio (Pasamos los datos limpios del DTO al Servicio)
        Tecnico nuevoTecnico = tecnicoService.crearTecnico(request.getNombre(), request.getEspecialidad());
        
        // 2. Mapear Dominio -> DTO y retornar con estado 201 Created
        return new ResponseEntity<>(TecnicoApiMapper.toResponseDTO(nuevoTecnico), HttpStatus.CREATED);
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