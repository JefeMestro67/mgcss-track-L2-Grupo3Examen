package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.TipoCliente;
import com.mgcss.infrastructure.ClienteRepositoryAdapter;

@DataJpaTest
@Tag("integration")
class ClienteRepositoryTest {

    @Autowired
    private JpaClienteRepository jpaRepository;

    @Test
    void debe_guardar_y_recuperar_un_cliente_en_h2() {
        // 1. ARRANGE
        ClienteEntity entity = new ClienteEntity();
        entity.setId(null);
        entity.setNombre("Juan Pérez");
        entity.setEmail("juan.perez@example.com");
        entity.setTipoCliente(TipoCliente.STANDARD);
        entity.setActivo(true);
        entity.setSolicitudesAbiertas(0);

        // 2. ACT
        ClienteEntity guardado = jpaRepository.save(entity);

        // 3. ASSERT
        Optional<ClienteEntity> recuperado = jpaRepository.findById(guardado.getId());
        
        assertTrue(recuperado.isPresent());
        assertEquals("Juan Pérez", recuperado.get().getNombre());
        assertEquals("juan.perez@example.com", recuperado.get().getEmail());
        assertEquals(0, recuperado.get().getSolicitudesAbiertas());
    }

    @Test
    void debe_funcionar_el_ciclo_completo_con_el_adaptador_de_cliente() {
        // 1. ARRANGE
        ClienteRepositoryAdapter adapter = new ClienteRepositoryAdapter(jpaRepository);
        
        Cliente clienteDominio = new Cliente();
        clienteDominio.setId(null);
        clienteDominio.setNombre("Ana Gómez");
        clienteDominio.setEmail("ana.gomez@example.com");
        clienteDominio.setTipoCliente(TipoCliente.STANDARD);
        clienteDominio.setActivo(true);
        clienteDominio.setSolicitudesAbiertas(2);

        // 2. ACT
        Cliente guardado = adapter.save(clienteDominio);
        Optional<Cliente> recuperado = adapter.findById(guardado.getId());

        // 3. ASSERT
        assertTrue(recuperado.isPresent());
        assertEquals("Ana Gómez", recuperado.get().getNombre());
        assertEquals("ana.gomez@example.com", recuperado.get().getEmail());
        assertEquals(2, recuperado.get().getSolicitudesAbiertas());
        assertTrue(recuperado.get().isActivo());
    }

    @Test
    void debe_retornar_vacio_si_el_cliente_no_existe() {
        ClienteRepositoryAdapter adapter = new ClienteRepositoryAdapter(jpaRepository);
        Optional<Cliente> recuperado = adapter.findById(999L);
        
        assertTrue(recuperado.isEmpty());
    }

    @Test
    void debe_actualizar_un_cliente() {
        ClienteRepositoryAdapter adapter = new ClienteRepositoryAdapter(jpaRepository);
        
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Inicial");
        cliente.setEmail("inicial@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(0);
        
        Cliente guardado = adapter.save(cliente);
        
        guardado.setNombre("Cliente Actualizado");
        Cliente actualizado = adapter.save(guardado);
        
        assertEquals("Cliente Actualizado", actualizado.getNombre());
    }
}
