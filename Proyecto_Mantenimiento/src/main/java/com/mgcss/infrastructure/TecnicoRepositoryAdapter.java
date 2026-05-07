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
        // La entidad (persistence) SÍ sigue teniendo setters, así que esto es correcto
        TecnicoEntity entity = new TecnicoEntity(
            tecnico.getId(),
            tecnico.getNombre(),
            tecnico.getEspecialidad(),
            tecnico.isActivo(),
            tecnico.getCargaTrabajo()
        );
        
        TecnicoEntity guardado = jpaRepository.save(entity);
        return mapToDomain(guardado);
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    private Tecnico mapToDomain(TecnicoEntity entity) {
        // Usamos el constructor completo del dominio porque ya no hay setters
        return new Tecnico(
            entity.getId(),
            entity.getNombre(),
            entity.getEspecialidad(),
            entity.isActivo(),
            entity.getCargaTrabajo()
        );
    }
}