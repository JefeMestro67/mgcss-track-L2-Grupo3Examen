package com.mgcss.api.controllers;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.api.mapper.SolicitudApiMapper;
import com.mgcss.domain.Solicitud;
import com.mgcss.services.SolicitudService;
import com.mgcss.infrastructure.SolicitudRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final SolicitudRepository solicitudRepository;

    public SolicitudController(SolicitudService solicitudService, SolicitudRepository solicitudRepository) {
        this.solicitudService = solicitudService;
        this.solicitudRepository = solicitudRepository;
    }

    // Paso 2.2: POST → crear solicitud
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crear(@RequestBody SolicitudRequestDTO request) {
        Solicitud nueva = solicitudService.crearSolicitud(request.getClienteId(), request.getDescripcion());
        return new ResponseEntity<>(SolicitudApiMapper.toResponseDTO(nueva), HttpStatus.CREATED);
    }

    // Paso 2.2: GET → consultar solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> consultar(@PathVariable Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
        return ResponseEntity.ok(SolicitudApiMapper.toResponseDTO(solicitud));
    }

    // Paso 2.2: PUT → asignar técnico (Cambiará estado a EN_PROCESO internamente)
    @PutMapping("/{id}/tecnico")
    public ResponseEntity<Void> asignarTecnico(@PathVariable Long id, @RequestParam Long tecnicoId) {
        solicitudService.asignarTecnico(id, tecnicoId);
        return ResponseEntity.noContent().build();
    }

    // Paso 2.2: PUT → cambiar estado a CERRADA
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<Void> cerrar(@PathVariable Long id) {
        solicitudService.cerrarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    // Paso 2.2: PATCH → reabrir solicitud
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrir(@PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    // Enunciado obligatorio: Listar solicitudes
    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        List<SolicitudResponseDTO> lista = solicitudRepository.findAll().stream()
                .map(SolicitudApiMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}

