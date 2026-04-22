package com.mgcss.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.mgcss.domain.Solicitud;
import com.mgcss.infrastructure.persistence.JpaSolicitudRepository;
import com.mgcss.infrastructure.persistence.SolicitudEntity;

@Repository 
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final JpaSolicitudRepository jpaRepository;

    public SolicitudRepositoryAdapter(JpaSolicitudRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        // 1. Traduce de Dominio a Entidad 
        SolicitudEntity entity = new SolicitudEntity(solicitud.getId(), solicitud.getEstado(), solicitud.getFechaCreacion());
        
        // 2. Guarda en la base de datos H2
        SolicitudEntity guardada = jpaRepository.save(entity);
        
        // 3. Devuelve un objeto de Dominio puro actualizado
        return new Solicitud(guardada.getId(), guardada.getEstado(), guardada.getFechaCreacion());
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        // 1. Busca en la base de datos
        Optional<SolicitudEntity> entityOpcional = jpaRepository.findById(id);
        
        // 2. Si lo encuentra, lo traduce a Dominio
        if (entityOpcional.isPresent()) {
            SolicitudEntity entity = entityOpcional.get();
            return Optional.of(new Solicitud(entity.getId(), entity.getEstado(), entity.getFechaCreacion()));
        }
        
        return Optional.empty();
    }
}