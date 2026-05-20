package com.mgcss.services;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    
    private static final String MSG_TECNICO_NO_EXISTE = "El técnico no existe";

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public void desactivarTecnico(Long id) {
        // 1. Recuperamos el técnico
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MSG_TECNICO_NO_EXISTE));

        // 2. El dominio ejecuta sus validaciones (ej. fallar si tiene carga > 0)
        tecnico.desactivar();

        // 3. Guardamos el cambio de forma persistente
        tecnicoRepository.save(tecnico);
    }
}
