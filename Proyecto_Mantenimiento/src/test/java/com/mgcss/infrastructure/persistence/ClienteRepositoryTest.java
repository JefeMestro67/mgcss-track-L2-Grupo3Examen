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
        ClienteEntity entity = new ClienteEntity();
        entity.setId(null);
        entity.setNombre("Juan Pérez");
        entity.setEmail("juan.perez@example.com");
        entity.setTipoCliente(TipoCliente.STANDARD);
        entity.setActivo(true);
        entity.setSolicitudesAbiertas(0);

        ClienteEntity guardado = jpaRepository.save(entity);

        Optional<ClienteEntity> recuperado = jpaRepository.findById(guardado.getId());
        
        assertTrue(recuperado.isPresent());
        assertEquals("Juan Pérez", recuperado.get().getNombre());
        assertEquals("juan.perez@example.com", recuperado.get().getEmail());
        assertEquals(0, recuperado.get().getSolicitudesAbiertas());
    }

    @Test
    void debe_funcionar_el_ciclo_completo_con_el_adaptador_de_cliente() {
        ClienteRepositoryAdapter adapter = new ClienteRepositoryAdapter(jpaRepository);
        
        Cliente clienteDominio = new Cliente(null, "Ana Gómez", "ana.gomez@example.com", TipoCliente.STANDARD, true, 2);

        Cliente guardado = adapter.save(clienteDominio);
        Optional<Cliente> recuperado = adapter.findById(guardado.getId());

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
        
        Cliente cliente = new Cliente(null, "Cliente Inicial", "inicial@example.com", TipoCliente.STANDARD, true, 0);
        Cliente guardado = adapter.save(cliente);
        
        // Al no tener setters, para actualizarlo tenemos que usar de nuevo su constructor simulando un cambio de nombre
        Cliente clienteAActualizar = new Cliente(guardado.getId(), "Cliente Actualizado", guardado.getEmail(), guardado.getTipoCliente(), guardado.isActivo(), guardado.getSolicitudesAbiertas());
        Cliente actualizado = adapter.save(clienteAActualizar);
        
        assertEquals("Cliente Actualizado", actualizado.getNombre());
    }
}