package com.mgcss.infrastructure;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Cliente;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.persistence.JpaSolicitudRepository;
import com.mgcss.infrastructure.persistence.JpaClienteRepository;
import com.mgcss.infrastructure.persistence.JpaTecnicoRepository;
import com.mgcss.infrastructure.persistence.SolicitudEntity;
import com.mgcss.infrastructure.persistence.ClienteEntity;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

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
        
        // Mapeo SEGURO de Cliente (Evita TransientPropertyValueException)
        if (solicitud.getCliente() != null && solicitud.getCliente().getId() != null) {
            ClienteEntity clienteEntity = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado en la base de datos"));
            entity.setCliente(clienteEntity);
        } else {
            throw new IllegalArgumentException("La solicitud debe estar asociada a un cliente existente con ID");
        }
        
        entity.setDescripcion(solicitud.getDescripcion());
        entity.setFechaCreacion(solicitud.getFechaCreacion());
        entity.setEstado(solicitud.getEstado());
        
        // Mapeo SEGURO de Tecnico buscando en base de datos primero
        if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
            TecnicoEntity tecnicoEntity = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new RuntimeException("Técnico no encontrado en la base de datos"));
            entity.setTecnicoAsignado(tecnicoEntity);
        }
        
        entity.setFechaCierre(solicitud.getFechaCierre());

        SolicitudEntity guardada = jpaRepository.save(entity);
        return mapToDomain(guardada); // Usamos un método privado para no duplicar código
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    // Extracción de lógica repetida para limpiar el código
    private Solicitud mapToDomain(SolicitudEntity entity) {
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
    }
}