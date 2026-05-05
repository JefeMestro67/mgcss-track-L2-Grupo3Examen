package com.mgcss.infrastructure;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.persistence.JpaTecnicoRepository;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

@Repository
public class TecnicoRepositoryAdapter implements TecnicoRepository {

    private final JpaTecnicoRepository jpaRepository;

    public TecnicoRepositoryAdapter(JpaTecnicoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Tecnico save(Tecnico tecnico) {
        // 1. Traduce de Dominio a Entidad usando los setters
        TecnicoEntity entity = new TecnicoEntity();
        entity.setId(tecnico.getId());
        entity.setNombre(tecnico.getNombre());
        entity.setEspecialidad(tecnico.getEspecialidad());
        entity.setActivo(tecnico.isActivo());
        entity.setCargaTrabajo(tecnico.getCargaTrabajo());

        // 2. Guarda en la base de datos H2
        TecnicoEntity guardado = jpaRepository.save(entity);

        // 3. Devuelve un objeto de Dominio puro actualizado
        Tecnico tecnicoGuardado = new Tecnico();
        tecnicoGuardado.setId(guardado.getId());
        tecnicoGuardado.setNombre(guardado.getNombre());
        tecnicoGuardado.setEspecialidad(guardado.getEspecialidad());
        tecnicoGuardado.setActivo(guardado.isActivo());
        tecnicoGuardado.setCargaTrabajo(guardado.getCargaTrabajo());

        return tecnicoGuardado;
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        // 1. Busca en la base de datos
        Optional<TecnicoEntity> entityOpcional = jpaRepository.findById(id);

        // 2. Si lo encuentra, lo traduce a Dominio usando setters
        return entityOpcional.map(entity -> {
            Tecnico tecnico = new Tecnico();
            tecnico.setId(entity.getId());
            tecnico.setNombre(entity.getNombre());
            tecnico.setEspecialidad(entity.getEspecialidad());
            tecnico.setActivo(entity.isActivo());
            tecnico.setCargaTrabajo(entity.getCargaTrabajo());
            
            return tecnico;
        });
    }
}