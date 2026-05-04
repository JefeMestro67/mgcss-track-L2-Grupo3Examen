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
        // 1. Traduce de Dominio a Entidad (incluyendo la carga de trabajo y nombre)
        TecnicoEntity entity = new TecnicoEntity(
            tecnico.getId(), 
            tecnico.getNombre(), 
            tecnico.isActivo(), 
            tecnico.getCargaTrabajo()
        );

        // 2. Guarda en la base de datos (H2 para los tests de la Sesión 7)
        TecnicoEntity guardado = jpaRepository.save(entity);

        // 3. Devuelve un objeto de Dominio actualizado
        return new Tecnico(
            guardado.getId(), 
            guardado.getNombre(), 
            guardado.isActivo(), 
            guardado.getCargaTrabajo()
        );
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        // 1. Busca en la base de datos
        Optional<TecnicoEntity> entityOpcional = jpaRepository.findById(id);

        // 2. Si lo encuentra, lo traduce a Dominio usando el nuevo constructor
        return entityOpcional.map(entity -> new Tecnico(
            entity.getId(), 
            entity.getNombre(), 
            entity.isActivo(), 
            entity.getCargaTrabajo()
        ));
    }
}
