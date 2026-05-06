package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Juan Pérez");
        clienteEntity.setEmail("juan.perez@example.com");
        clienteEntity.setTipoCliente(TipoCliente.STANDARD);
        clienteEntity.setActivo(true);
        clienteEntity.setSolicitudesAbiertas(0);
        
        ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);

        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(null);
        entity.setCliente(clienteGuardado);
        entity.setEstado(Estado.ABIERTA);
        entity.setDescripcion("Descripción de prueba");
        entity.setFechaCreacion(LocalDateTime.now());

        SolicitudEntity guardada = repository.save(entity);

        Optional<SolicitudEntity> recuperada = repository.findById(guardada.getId());
        assertTrue(recuperada.isPresent());
        assertEquals(Estado.ABIERTA, recuperada.get().getEstado());
        assertEquals("Juan Pérez", recuperada.get().getCliente().getNombre());
    }
    
    @Test
    void debe_guardar_y_recuperar_usando_el_adaptador_completo() {
        SolicitudRepositoryAdapter adapter = new SolicitudRepositoryAdapter(repository, clienteRepository);
        
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Ana Gómez");
        clienteEntity.setEmail("ana.gomez@example.com");
        clienteEntity.setTipoCliente(TipoCliente.STANDARD);
        clienteEntity.setActivo(true);
        clienteEntity.setSolicitudesAbiertas(0);
        
        ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);
        
        Solicitud solicitudDominio = new Solicitud();
        solicitudDominio.setId(null);
        solicitudDominio.setEstado(Estado.ABIERTA);
        solicitudDominio.setDescripcion("Descripción desde el adaptador");
        
        Cliente clienteDominio = new Cliente();
        clienteDominio.setId(clienteGuardado.getId());
        clienteDominio.setNombre(clienteGuardado.getNombre());
        clienteDominio.setEmail(clienteGuardado.getEmail());
        clienteDominio.setTipoCliente(clienteGuardado.getTipoCliente());
        clienteDominio.setActivo(clienteGuardado.isActivo());
        clienteDominio.setSolicitudesAbiertas(clienteGuardado.getSolicitudesAbiertas());
        solicitudDominio.setCliente(clienteDominio);

        Solicitud guardada = adapter.save(solicitudDominio);
        
        Optional<Solicitud> recuperada = adapter.findById(guardada.getId());
        assertTrue(recuperada.isPresent());
        assertEquals(Estado.ABIERTA, recuperada.get().getEstado());
        assertEquals(guardada.getId(), recuperada.get().getId());
        assertEquals(clienteGuardado.getId(), recuperada.get().getCliente().getId());
    }

    @Test
    void debe_retornar_vacio_si_la_solicitud_no_existe() {
        SolicitudRepositoryAdapter adapter = new SolicitudRepositoryAdapter(repository, clienteRepository);
        Optional<Solicitud> recuperada = adapter.findById(999L);
        
        assertTrue(recuperada.isEmpty());
    }

    @Test
    void debe_lanzar_excepcion_al_guardar_sin_cliente() {
        SolicitudRepositoryAdapter adapter = new SolicitudRepositoryAdapter(repository, clienteRepository);
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(Estado.ABIERTA);
        
        assertThrows(IllegalArgumentException.class, () -> adapter.save(solicitud));
    }
    @Test
    void debe_lanzar_excepcion_si_cliente_no_tiene_id() {
        SolicitudRepositoryAdapter adapter = new SolicitudRepositoryAdapter(repository, clienteRepository);
        Solicitud solicitud = new Solicitud();
        solicitud.setCliente(new Cliente()); // Cliente sin ID
        solicitud.setEstado(Estado.ABIERTA);
        
        assertThrows(IllegalArgumentException.class, () -> adapter.save(solicitud));
    }
}
