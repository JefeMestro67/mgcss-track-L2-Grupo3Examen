package com.mgcss.services;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.TecnicoRepository;
import org.springframework.stereotype.Service;

public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    // Inyección por constructor para permitir el testeo en aislamiento
    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public void asignarNuevaTarea(Long id) {
        
        // 1. Protegemos la búsqueda: si el Optional está vacío, lanzamos nuestra excepción
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));

        // 2. El servicio llama al dominio
        tecnico.incrementarCarga();

        // 3. Guardamos el estado final
        tecnicoRepository.save(tecnico);
    }

    public void desactivarTecnico(Long id) {
        
        // 1. Recuperamos el técnico
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));

        // 2. El servicio llama al dominio
        tecnico.desactivar();

        // 3. Guardamos el cambio
        tecnicoRepository.save(tecnico);
    }

    public void finalizarTarea(Long id) {
        
        // 1. Recuperamos el técnico
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));

        // 2. El servicio llama al dominio
        tecnico.finalizarTarea();

        // 3. Guardamos el cambio
        tecnicoRepository.save(tecnico);
    }
}
