package com.mgcss.infrastructure;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Cliente;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.persistence.JpaSolicitudRepository;
import com.mgcss.infrastructure.persistence.JpaClienteRepository;
import com.mgcss.infrastructure.persistence.SolicitudEntity;
import com.mgcss.infrastructure.persistence.ClienteEntity;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

@Repository 
@Transactional
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final JpaSolicitudRepository jpaRepository;
    private final JpaClienteRepository clienteRepository;

    public SolicitudRepositoryAdapter(JpaSolicitudRepository jpaRepository, JpaClienteRepository clienteRepository) {
        this.jpaRepository = jpaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        // 1. Traduce de Dominio a Entidad usando los setters
        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(solicitud.getId());
        
        // Mapeo de la relación Cliente
        if (solicitud.getCliente() != null) {
            if (solicitud.getCliente().getId() == null) {
                throw new IllegalArgumentException("La solicitud debe estar asociada a un cliente existente con ID");
            }
            
            // Obtenemos el cliente desde la base de datos (evita el error de entidad transitoria)
            ClienteEntity clienteEntity = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado en la base de datos"));
                    
            entity.setCliente(clienteEntity);
        } else {
            throw new IllegalArgumentException("La solicitud debe tener un cliente");
        }
        
        entity.setDescripcion(solicitud.getDescripcion());
        entity.setFechaCreacion(solicitud.getFechaCreacion());
        entity.setEstado(solicitud.getEstado());
        
        // Mapeo de la relación Tecnico
        if (solicitud.getTecnicoAsignado() != null) {
            TecnicoEntity tecnicoEntity = new TecnicoEntity();
            tecnicoEntity.setId(solicitud.getTecnicoAsignado().getId());
            tecnicoEntity.setNombre(solicitud.getTecnicoAsignado().getNombre());
            tecnicoEntity.setEspecialidad(solicitud.getTecnicoAsignado().getEspecialidad());
            tecnicoEntity.setActivo(solicitud.getTecnicoAsignado().isActivo());
            tecnicoEntity.setCargaTrabajo(solicitud.getTecnicoAsignado().getCargaTrabajo());
            entity.setTecnicoAsignado(tecnicoEntity);
        }
        
        entity.setFechaCierre(solicitud.getFechaCierre());

        // 2. Guarda en la base de datos
        SolicitudEntity guardada = jpaRepository.save(entity);

        // 3. Devuelve un objeto de Dominio puro actualizado
        Solicitud solicitudGuardada = new Solicitud();
        solicitudGuardada.setId(guardada.getId());
        
        if (guardada.getCliente() != null) {
            Cliente clienteDominio = new Cliente();
            clienteDominio.setId(guardada.getCliente().getId());
            clienteDominio.setNombre(guardada.getCliente().getNombre());
            clienteDominio.setEmail(guardada.getCliente().getEmail());
            clienteDominio.setTipoCliente(guardada.getCliente().getTipoCliente());
            clienteDominio.setActivo(guardada.getCliente().isActivo());
            clienteDominio.setSolicitudesAbiertas(guardada.getCliente().getSolicitudesAbiertas());
            solicitudGuardada.setCliente(clienteDominio);
        }
        
        solicitudGuardada.setDescripcion(guardada.getDescripcion());
        solicitudGuardada.setFechaCreacion(guardada.getFechaCreacion());
        solicitudGuardada.setEstado(guardada.getEstado());
        
        if (guardada.getTecnicoAsignado() != null) {
            Tecnico tecnicoDominio = new Tecnico();
            tecnicoDominio.setId(guardada.getTecnicoAsignado().getId());
            tecnicoDominio.setNombre(guardada.getTecnicoAsignado().getNombre());
            tecnicoDominio.setEspecialidad(guardada.getTecnicoAsignado().getEspecialidad());
            tecnicoDominio.setActivo(guardada.getTecnicoAsignado().isActivo());
            tecnicoDominio.setCargaTrabajo(guardada.getTecnicoAsignado().getCargaTrabajo());
            solicitudGuardada.setTecnicoAsignado(tecnicoDominio);
        }
        
        solicitudGuardada.setFechaCierre(guardada.getFechaCierre());
        
        return solicitudGuardada;
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        // 1. Busca en la base de datos
        Optional<SolicitudEntity> entityOpcional = jpaRepository.findById(id);

        // 2. Si lo encuentra, lo traduce a Dominio usando setters
        return entityOpcional.map(entity -> {
            Solicitud solicitud = new Solicitud();
            solicitud.setId(entity.getId());
            
            if (entity.getCliente() != null) {
                Cliente clienteDominio = new Cliente();
                clienteDominio.setId(entity.getCliente().getId());
                clienteDominio.setNombre(entity.getCliente().getNombre());
                clienteDominio.setEmail(entity.getCliente().getEmail());
                clienteDominio.setTipoCliente(entity.getCliente().getTipoCliente());
                clienteDominio.setActivo(entity.getCliente().isActivo());
                clienteDominio.setSolicitudesAbiertas(entity.getCliente().getSolicitudesAbiertas());
                solicitud.setCliente(clienteDominio);
            }
            
            solicitud.setDescripcion(entity.getDescripcion());
            solicitud.setFechaCreacion(entity.getFechaCreacion());
            solicitud.setEstado(entity.getEstado());
            
            if (entity.getTecnicoAsignado() != null) {
                Tecnico tecnicoDominio = new Tecnico();
                tecnicoDominio.setId(entity.getTecnicoAsignado().getId());
                tecnicoDominio.setNombre(entity.getTecnicoAsignado().getNombre());
                tecnicoDominio.setEspecialidad(entity.getTecnicoAsignado().getEspecialidad());
                tecnicoDominio.setActivo(entity.getTecnicoAsignado().isActivo());
                tecnicoDominio.setCargaTrabajo(entity.getTecnicoAsignado().getCargaTrabajo());
                solicitud.setTecnicoAsignado(tecnicoDominio);
            }
            
            solicitud.setFechaCierre(entity.getFechaCierre());
            
            return solicitud;
        });
    }
}