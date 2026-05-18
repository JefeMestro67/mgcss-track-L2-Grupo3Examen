package com.mgcss.api.controllers;

import com.mgcss.services.TecnicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    // PUT → Desactivar un técnico
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        tecnicoService.desactivarTecnico(id);
        return ResponseEntity.noContent().build();
    }

    // PUT → Incrementar carga (Asignar nueva tarea manualmente)
    @PutMapping("/{id}/asignar-tarea")
    public ResponseEntity<Void> asignarTarea(@PathVariable Long id) {
        tecnicoService.asignarNuevaTarea(id);
        return ResponseEntity.noContent().build();
    }

    // PUT → Finalizar tarea (Reducir carga de trabajo)
    @PutMapping("/{id}/finalizar-tarea")
    public ResponseEntity<Void> finalizarTarea(@PathVariable Long id) {
        tecnicoService.finalizarTarea(id);
        return ResponseEntity.noContent().build();
    }
}