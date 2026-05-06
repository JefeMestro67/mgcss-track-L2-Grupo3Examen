package com.mgcss.infrastructure;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.mgcss.domain.Cliente;
import com.mgcss.infrastructure.persistence.ClienteEntity;
import com.mgcss.infrastructure.persistence.JpaClienteRepository;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaRepository;

    public ClienteRepositoryAdapter(JpaClienteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        // 1. Traduce de Dominio a Entidad (incluyendo tipoCliente)
        ClienteEntity entity = new ClienteEntity(
            cliente.getId(), 
            cliente.getNombre(), 
            cliente.getEmail(), 
            cliente.getTipoCliente(), 
            cliente.isActivo(), 
            cliente.getSolicitudesAbiertas()
        );

        // 2. Guarda en la base de datos
        ClienteEntity guardado = jpaRepository.save(entity);

        // 3. Devuelve un objeto de Dominio actualizado
        return new Cliente(
            guardado.getId(), 
            guardado.getNombre(), 
            guardado.getEmail(), 
            guardado.getTipoCliente(), 
            guardado.isActivo(), 
            guardado.getSolicitudesAbiertas()
        );
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        // 1. Busca en la base de datos
        Optional<ClienteEntity> entityOpcional = jpaRepository.findById(id);

        // 2. Si lo encuentra, lo traduce a Dominio
        return entityOpcional.map(entity -> new Cliente(
            entity.getId(), 
            entity.getNombre(), 
            entity.getEmail(), 
            entity.getTipoCliente(), 
            entity.isActivo(), 
            entity.getSolicitudesAbiertas()
        ));
    }
}
