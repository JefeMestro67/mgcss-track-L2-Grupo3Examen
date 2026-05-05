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
        // 1. ARRANGE: Creamos la entidad de infraestructura usando el constructor vacío y setters
        ClienteEntity entity = new ClienteEntity();
        entity.setId(null);
        entity.setNombre("Juan Pérez");
        entity.setEmail("juan.perez@example.com");
        entity.setTipoCliente(TipoCliente.STANDARD);
        entity.setActivo(true);
        entity.setSolicitudesAbiertas(0);

        // 2. ACT: Guardamos directamente con el repositorio JPA
        ClienteEntity guardado = jpaRepository.save(entity);

        // 3. ASSERT: Comprobamos la persistencia real
        Optional<ClienteEntity> recuperado = jpaRepository.findById(guardado.getId());
        
        assertTrue(recuperado.isPresent());
        assertEquals("Juan Pérez", recuperado.get().getNombre());
        assertEquals("juan.perez@example.com", recuperado.get().getEmail());
        assertEquals(0, recuperado.get().getSolicitudesAbiertas());
    }

    @Test
    void debe_funcionar_el_ciclo_completo_con_el_adaptador_de_cliente() {
        // 1. ARRANGE: Instanciamos el adaptador manual
        ClienteRepositoryAdapter adapter = new ClienteRepositoryAdapter(jpaRepository);
        
        // Objeto de DOMINIO
        Cliente clienteDominio = new Cliente();
        clienteDominio.setId(null);
        clienteDominio.setNombre("Ana Gómez");
        clienteDominio.setEmail("ana.gomez@example.com");
        clienteDominio.setTipoCliente(TipoCliente.STANDARD);
        clienteDominio.setActivo(true);
        clienteDominio.setSolicitudesAbiertas(2);

        // 2. ACT: Guardar y Recuperar a través del Adaptador
        Cliente guardado = adapter.save(clienteDominio);
        Optional<Cliente> recuperado = adapter.findById(guardado.getId());

        // 3. ASSERT: Verificamos que el mapeo Dominio -> Entity -> Dominio es correcto
        assertTrue(recuperado.isPresent());
        assertEquals("Ana Gómez", recuperado.get().getNombre());
        assertEquals("ana.gomez@example.com", recuperado.get().getEmail());
        assertEquals(2, recuperado.get().getSolicitudesAbiertas());
        assertTrue(recuperado.get().isActivo());
    }
}
