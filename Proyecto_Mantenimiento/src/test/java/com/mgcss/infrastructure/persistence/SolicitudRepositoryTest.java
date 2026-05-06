package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Estado;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.SolicitudRepositoryAdapter;

@DataJpaTest
@Tag("integration")
class SolicitudRepositoryTest {

    @Autowired
    private JpaSolicitudRepository repository;

    @Autowired
    private JpaClienteRepository clienteRepository;

    @Autowired
    private JpaTecnicoRepository tecnicoRepository; // Inyectado para evitar errores de integridad

    private SolicitudRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        // Le añadimos el tecnicoRepository que ya tenías inyectado en la clase
        adapter = new SolicitudRepositoryAdapter(repository, clienteRepository, tecnicoRepository);
    }

    @Test
    void debe_mapear_y_cubrir_todos_los_bloques_de_tecnico() {
        // 1. ARRANGE: Guardar Cliente en DB
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Cliente de Prueba");
        clienteEntity.setActivo(true);
        clienteEntity = clienteRepository.save(clienteEntity);

        // 2. ARRANGE: Guardar Técnico en DB (Esto evita el error DataIntegrityViolationException)
        TecnicoEntity tecnicoE = new TecnicoEntity();
        tecnicoE.setNombre("Carlos Técnico");
        tecnicoE.setEspecialidad("Sistemas");
        tecnicoE.setActivo(true);
        tecnicoE.setCargaTrabajo(0);
        tecnicoE = tecnicoRepository.save(tecnicoE); 

        // 3. ARRANGE: Preparar objetos de dominio para el Adapter
        Cliente clienteD = new Cliente();
        clienteD.setId(clienteEntity.getId());

        Tecnico tecnicoD = new Tecnico();
        tecnicoD.setId(tecnicoE.getId()); // Usamos el ID real generado por la base de datos
        tecnicoD.setNombre(tecnicoE.getNombre());
        tecnicoD.setEspecialidad(tecnicoE.getEspecialidad());
        tecnicoD.setActivo(tecnicoE.isActivo());
        tecnicoD.setCargaTrabajo(tecnicoE.getCargaTrabajo());

        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion("Fallo en placa base");
        solicitud.setEstado(Estado.EN_PROCESO);
        solicitud.setCliente(clienteD);
        solicitud.setTecnicoAsignado(tecnicoD); 

        // 4. ACT: Guardar (Cubre mapeo de entrada y salida del save)
        Solicitud guardada = adapter.save(solicitud);

        // 5. ACT: Buscar (Cubre mapeo dentro del findById)
        Optional<Solicitud> encontradaOpcional = adapter.findById(guardada.getId());

        // 6. ASSERT: Verificaciones para el covefran
        assertTrue(encontradaOpcional.isPresent());
        Solicitud s = encontradaOpcional.get();
        assertNotNull(s.getTecnicoAsignado());
        assertEquals("Carlos Técnico", s.getTecnicoAsignado().getNombre());
        assertEquals("Sistemas", s.getTecnicoAsignado().getEspecialidad());
        assertTrue(s.getTecnicoAsignado().isActivo());
    }

    @Test
    void debe_lanzar_excepcion_al_guardar_sin_cliente() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(Estado.ABIERTA);
        
        // Verifica el throw del adaptador cuando el cliente es null
        assertThrows(IllegalArgumentException.class, () -> adapter.save(solicitud));
    }

    @Test
    void debe_lanzar_excepcion_si_cliente_no_existe_en_db() {
        Cliente clienteD = new Cliente();
        clienteD.setId(999L); // ID inexistente

        Solicitud solicitud = new Solicitud();
        solicitud.setCliente(clienteD);
        solicitud.setDescripcion("Test");

        // Verifica el orElseThrow cuando el cliente no está en la tabla
        assertThrows(RuntimeException.class, () -> adapter.save(solicitud));
    }

    @Test
    void debe_retornar_vacio_si_la_solicitud_no_existe() {
        Optional<Solicitud> recuperada = adapter.findById(888L);
        assertTrue(recuperada.isEmpty());
    }
}
