package com.mgcss.services;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.TecnicoRepository;
import org.springframework.stereotype.Service;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private static final String MSG_TECNICO_NO_EXISTE = "El técnico no existe";

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public void desactivarTecnico(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MSG_TECNICO_NO_EXISTE));
        tecnico.desactivar();
        tecnicoRepository.save(tecnico);
    }
    
    public Tecnico crearTecnico(String nombre, String especialidad) {
        Tecnico nuevoTecnico = new Tecnico(
            null, 
            nombre, 
            especialidad, 
            true, 
            0
        );
        return tecnicoRepository.save(nuevoTecnico);
    }

}