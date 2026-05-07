package com.mgcss.infrastructure;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.mgcss.domain.Cliente;
import com.mgcss.infrastructure.persistence.JpaClienteRepository;
import com.mgcss.infrastructure.persistence.ClienteEntity;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaRepository;

    public ClienteRepositoryAdapter(JpaClienteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        // Mapeo a entidad usando su constructor
        ClienteEntity entity = new ClienteEntity(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTipoCliente(),
            cliente.isActivo(),
            cliente.getSolicitudesAbiertas()
        );
        
        ClienteEntity guardado = jpaRepository.save(entity);
        return mapToDomain(guardado);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    private Cliente mapToDomain(ClienteEntity entity) {
        // Mapeo a dominio usando su constructor completo
        return new Cliente(
            entity.getId(),
            entity.getNombre(),
            entity.getEmail(),
            entity.getTipoCliente(),
            entity.isActivo(),
            entity.getSolicitudesAbiertas()
        );
    }
}