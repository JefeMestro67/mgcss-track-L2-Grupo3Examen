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

    //  NUEBO MÉTODO AGREGADO PARA EL PASO 2.2
    @Transactional
    public Tecnico crearTecnico(String nombre, String especialidad) {
        // 1. Validaciones básicas en la capa de aplicación
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del técnico es obligatorio");
        }
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad del técnico es obligatoria");
        }

        // 2. Instanciar el objeto de dominio (Nace activo y con carga 0 por defecto en su lógica interna)
        // Nota: Asegúrate de que tu entidad 'Tecnico' tenga este constructor: (id, nombre, especialidad, activo, cargaTrabajo)
        Tecnico nuevoTecnico = new Tecnico(null, nombre, especialidad, true, 0);

        // 3. Persistir en la base de datos a través del repositorio y devolver la entidad gestionada
        return tecnicoRepository.save(nuevoTecnico);
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
