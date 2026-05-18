package com.mgcss.api.controllers;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.api.mapper.SolicitudApiMapper;
import com.mgcss.domain.Solicitud;
import com.mgcss.services.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes", description = "Controlador para la gestión del ciclo de vida de las solicitudes de mantenimiento")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud", description = "Registra una solicitud de mantenimiento asociada a un cliente existente. Nace automáticamente en estado ABIERTA.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud creada con éxito de manera persistente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o inconsistentes"),
        @ApiResponse(responseCode = "500", description = "Error interno: El cliente indicado no existe en el sistema")
    })
    public ResponseEntity<SolicitudResponseDTO> crear(@RequestBody SolicitudRequestDTO request) {
        Solicitud nueva = solicitudService.crearSolicitud(request.getClienteId(), request.getDescripcion());
        return new ResponseEntity<>(SolicitudApiMapper.toResponseDTO(nueva), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar solicitud por ID", description = "Recupera los detalles completos de una solicitud específica a partir de su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada y devuelta con éxito"),
        @ApiResponse(responseCode = "404", description = "La solicitud con el ID proporcionado no existe en el sistema")
    })
    public ResponseEntity<SolicitudResponseDTO> consultar(
            @Parameter(description = "ID único de la solicitud a consultar", example = "1") @PathVariable Long id) {
        Solicitud solicitud = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(SolicitudApiMapper.toResponseDTO(solicitud));
    }

    @PutMapping("/{id}/tecnico")
    @Operation(summary = "Asignar un técnico", description = "Asigna un operario técnico a la solicitud. Provoca que el estado de la solicitud transicione automáticamente a EN_PROCESO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Técnico asignado correctamente. Estado mutado a EN_PROCESO"),
        @ApiResponse(responseCode = "400", description = "Regla de negocio violada o IDs inválidos")
    })
    public ResponseEntity<Void> asignarTecnico(
            @Parameter(description = "ID de la solicitud", example = "1") @PathVariable Long id, 
            @Parameter(description = "ID del técnico que se hará cargo", example = "2") @RequestParam Long tecnicoId) {
        solicitudService.asignarTecnico(id, tecnicoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cerrar")
    @Operation(summary = "Cerrar una solicitud", description = "Cambia el estado de una solicitud a CERRADA y registra la fecha de finalización. Requiere obligatoriamente que la solicitud haya pasado previamente por el estado EN_PROCESO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Solicitud cerrada con éxito"),
        @ApiResponse(responseCode = "500", description = "Error de negocio: Intento de cierre ilegal desde un estado no permitido (ej. ABIERTA)")
    })
    public ResponseEntity<Void> cerrar(
            @Parameter(description = "ID de la solicitud que se desea cerrar", example = "1") @PathVariable Long id) {
        solicitudService.cerrarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Reabrir una solicitud cerrada", description = "Permite la reapertura manual de una incidencia previamente CERRADA, devolviéndola al estado ABIERTA.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Solicitud reabierta con éxito"),
        @ApiResponse(responseCode = "400", description = "La solicitud no se puede reabrir porque no se encontraba en estado CERRADA")
    })
    public ResponseEntity<Void> reabrir(
            @Parameter(description = "ID de la solicitud a reabrir", example = "1") @PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes", description = "Retorna una lista completa con el histórico de solicitudes registradas en la aplicación.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        List<SolicitudResponseDTO> lista = solicitudService.listarTodas().stream()
                .map(SolicitudApiMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}

