package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Estado;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.TipoCliente;
import com.mgcss.infrastructure.SolicitudRepositoryAdapter;

@DataJpaTest
@Tag("integration")
class SolicitudRepositoryTest {

    @Autowired
    private JpaSolicitudRepository repository;

    @Autowired
    private JpaClienteRepository clienteRepository;

    @Test
    void debe_guardar_y_recuperar_una_solicitud_real() {
        // ARRANGE: Primero creamos y guardamos un cliente para cumplir la restricción not-null
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Juan Pérez");
        clienteEntity.setEmail("juan.perez@example.com");
        clienteEntity.setTipoCliente(TipoCliente.STANDARD);
        clienteEntity.setActivo(true);
        clienteEntity.setSolicitudesAbiertas(0);
        
        ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);

        // Creamos la solicitud y le asociamos el cliente persistido
        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(null);
        entity.setCliente(clienteGuardado);
        entity.setEstado(Estado.ABIERTA);
        entity.setDescripcion("Descripción de prueba");
        entity.setFechaCreacion(LocalDateTime.now());

        // ACT: Guardamos en H2
        SolicitudEntity guardada = repository.save(entity);

        // ASSERT: Verificamos que el cliente exista en la solicitud recuperada
        Optional<SolicitudEntity> recuperada = repository.findById(guardada.getId());
        assertTrue(recuperada.isPresent());
        assertEquals(Estado.ABIERTA, recuperada.get().getEstado());
        assertEquals("Juan Pérez", recuperada.get().getCliente().getNombre());
    }
    
    @Test
    void debe_guardar_y_recuperar_usando_el_adaptador_completo() {
        SolicitudRepositoryAdapter adapter = new SolicitudRepositoryAdapter(repository, clienteRepository);
        
        // ARRANGE: Preparamos un cliente persistido para el adaptador
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Ana Gómez");
        clienteEntity.setEmail("ana.gomez@example.com");
        clienteEntity.setTipoCliente(TipoCliente.STANDARD);
        clienteEntity.setActivo(true);
        clienteEntity.setSolicitudesAbiertas(0);
        
        ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);
        
        // Creamos la solicitud de Dominio
        Solicitud solicitudDominio = new Solicitud();
        solicitudDominio.setId(null);
        solicitudDominio.setEstado(Estado.ABIERTA);
        solicitudDominio.setDescripcion("Descripción desde el adaptador");
        
        // Asociamos el cliente de dominio
        Cliente clienteDominio = new Cliente();
        clienteDominio.setId(clienteGuardado.getId());
        clienteDominio.setNombre(clienteGuardado.getNombre());
        clienteDominio.setEmail(clienteGuardado.getEmail());
        clienteDominio.setTipoCliente(clienteGuardado.getTipoCliente());
        clienteDominio.setActivo(clienteGuardado.isActivo());
        clienteDominio.setSolicitudesAbiertas(clienteGuardado.getSolicitudesAbiertas());
        solicitudDominio.setCliente(clienteDominio);

        // ACT: Guardamos la solicitud a través del adaptador
        Solicitud guardada = adapter.save(solicitudDominio);
        
        // ASSERT
        Optional<Solicitud> recuperada = adapter.findById(guardada.getId());
        assertTrue(recuperada.isPresent());
        assertEquals(Estado.ABIERTA, recuperada.get().getEstado());
        assertEquals(guardada.getId(), recuperada.get().getId());
        assertEquals(clienteGuardado.getId(), recuperada.get().getCliente().getId());
    }
}
