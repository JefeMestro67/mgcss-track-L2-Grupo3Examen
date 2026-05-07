package com.mgcss.infrastructure;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mgcss.domain.*;
import com.mgcss.infrastructure.persistence.*;

@Repository 
@Transactional
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final JpaSolicitudRepository jpaRepository;
    private final JpaClienteRepository clienteRepository;
    private final JpaTecnicoRepository tecnicoRepository;

    public SolicitudRepositoryAdapter(JpaSolicitudRepository jpaRepository, 
                                      JpaClienteRepository clienteRepository,
                                      JpaTecnicoRepository tecnicoRepository) {
        this.jpaRepository = jpaRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(solicitud.getId());
        
        if (solicitud.getCliente() != null && solicitud.getCliente().getId() != null) {
            ClienteEntity clienteEntity = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            entity.setCliente(clienteEntity);
        } else {
            throw new IllegalArgumentException("La solicitud requiere un cliente con ID");
        }
        
        entity.setDescripcion(solicitud.getDescripcion());
        entity.setFechaCreacion(solicitud.getFechaCreacion());
        entity.setEstado(solicitud.getEstado());
        
        if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
            TecnicoEntity tecnicoEntity = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
            entity.setTecnicoAsignado(tecnicoEntity);
        }
        
        entity.setFechaCierre(solicitud.getFechaCierre());

        // --- MAPEO DEL HISTORIAL (Dominio -> Entidad) ---
        List<EstadoChangeEntity> historialEntities = solicitud.getHistorial().stream()
                .map(h -> new EstadoChangeEntity(h.getEstadoAnterior(), h.getEstadoNuevo(), h.getFechaCambio()))
                .collect(Collectors.toList());
        entity.setHistorial(historialEntities);
        // ------------------------------------------------

        SolicitudEntity guardada = jpaRepository.save(entity);
        return mapToDomain(guardada);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    private Solicitud mapToDomain(SolicitudEntity entity) {
        Cliente clienteDominio = null;
        if (entity.getCliente() != null) {
            clienteDominio = new Cliente(
                entity.getCliente().getId(),
                entity.getCliente().getNombre(),
                entity.getCliente().getEmail(),
                entity.getCliente().getTipoCliente(),
                entity.getCliente().isActivo(),
                entity.getCliente().getSolicitudesAbiertas()
            );
        }
        
        Tecnico tecnicoDominio = null;
        if (entity.getTecnicoAsignado() != null) {
            tecnicoDominio = new Tecnico(
                entity.getTecnicoAsignado().getId(),
                entity.getTecnicoAsignado().getNombre(),
                entity.getTecnicoAsignado().getEspecialidad(),
                entity.getTecnicoAsignado().isActivo(),
                entity.getTecnicoAsignado().getCargaTrabajo()
            );
        }
        
        // Creamos el objeto de dominio
        Solicitud solicitud = new Solicitud(
            entity.getId(),
            clienteDominio,
            entity.getDescripcion(),
            entity.getFechaCreacion(),
            entity.getEstado(),
            tecnicoDominio,
            entity.getFechaCierre()
        );

        // --- MAPEO DEL HISTORIAL (Entidad -> Dominio) ---
        // Como el historial en Solicitud es privado, lo restauramos mediante los cambios guardados
        if (entity.getHistorial() != null) {
            entity.getHistorial().forEach(h -> 
                solicitud.getHistorial().add(new EstadoChange(h.getEstadoAnterior(), h.getEstadoNuevo()))
            );
        }
        // ------------------------------------------------

        return solicitud;
    }
}