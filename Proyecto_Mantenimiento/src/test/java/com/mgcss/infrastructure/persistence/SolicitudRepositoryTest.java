package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
import com.mgcss.domain.TipoCliente;
import com.mgcss.infrastructure.SolicitudRepositoryAdapter;

@DataJpaTest
@Tag("integration")
class SolicitudRepositoryTest {

    @Autowired
    private JpaSolicitudRepository repository;

    @Autowired
    private JpaClienteRepository clienteRepository;

    @Autowired
    private JpaTecnicoRepository tecnicoRepository; 

    private SolicitudRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SolicitudRepositoryAdapter(repository, clienteRepository, tecnicoRepository);
    }

    @Test
    void debe_persistir_y_recuperar_el_historial_de_estados() {
        // 1. Setup de entidades reales en DB
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Test Historial");
        clienteEntity.setActivo(true);
        clienteEntity = clienteRepository.save(clienteEntity);

        TecnicoEntity tecnicoE = new TecnicoEntity();
        tecnicoE.setNombre("Tecnico Historial");
        tecnicoE.setActivo(true);
        tecnicoE = tecnicoRepository.save(tecnicoE);

        Cliente clienteD = new Cliente(clienteEntity.getId(), "Test Historial", null, TipoCliente.STANDARD, true, 0);
        Tecnico tecnicoD = new Tecnico(tecnicoE.getId(), "Tecnico Historial", "Sistemas", true, 0);

        // 2. Provocamos cambios en el DOMINIO para generar historial
        Solicitud solicitud = new Solicitud(null, clienteD, "Fallo", LocalDateTime.now(), Estado.ABIERTA, null, null);
        solicitud.asignarTecnico(tecnicoD); // Cambio 1
        solicitud.cerrar();                // Cambio 2
        solicitud.reabrir();               // Cambio 3

        // 3. GUARDAR (Prueba el mapeo Dominio -> Entidad)
        Solicitud guardada = adapter.save(solicitud);
        
        // 4. RECUPERAR (Prueba el mapeo Entidad -> Dominio)
        Solicitud recuperada = adapter.findById(guardada.getId()).get();

        // 5. VERIFICAR
        assertEquals(3, recuperada.getHistorial().size());
        assertEquals(Estado.ABIERTA, recuperada.getHistorial().get(0).getEstadoAnterior());
        assertEquals(Estado.EN_PROCESO, recuperada.getHistorial().get(0).getEstadoNuevo());
        assertEquals(Estado.CERRADA, recuperada.getHistorial().get(2).getEstadoAnterior());
    }

    @Test
    void debe_mapear_y_cubrir_todos_los_bloques_de_tecnico() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("Cliente de Prueba");
        clienteEntity.setActivo(true);
        clienteEntity = clienteRepository.save(clienteEntity);

        TecnicoEntity tecnicoE = new TecnicoEntity();
        tecnicoE.setNombre("Carlos Técnico");
        tecnicoE.setEspecialidad("Sistemas");
        tecnicoE.setActivo(true);
        tecnicoE.setCargaTrabajo(0);
        tecnicoE = tecnicoRepository.save(tecnicoE); 

        Cliente clienteD = new Cliente(clienteEntity.getId(), "Cliente de Prueba", null, TipoCliente.STANDARD, true, 0);
        Tecnico tecnicoD = new Tecnico(tecnicoE.getId(), "Carlos Técnico", "Sistemas", true, 0);

        Solicitud solicitud = new Solicitud(null, clienteD, "Fallo en placa base", LocalDateTime.now(), Estado.EN_PROCESO, tecnicoD, null);

        Solicitud guardada = adapter.save(solicitud);
        Optional<Solicitud> encontradaOpcional = adapter.findById(guardada.getId());

        assertTrue(encontradaOpcional.isPresent());
        Solicitud s = encontradaOpcional.get();
        assertNotNull(s.getTecnicoAsignado());
        assertEquals("Carlos Técnico", s.getTecnicoAsignado().getNombre());
    }

    @Test
    void debe_lanzar_excepcion_al_guardar_sin_cliente() {
        Solicitud solicitud = new Solicitud(null, null, "Sin cliente", LocalDateTime.now(), Estado.ABIERTA, null, null);
        assertThrows(IllegalArgumentException.class, () -> adapter.save(solicitud));
    }

    @Test
    void debe_lanzar_excepcion_si_cliente_no_existe_en_db() {
        Cliente clienteD = new Cliente(999L, "Falso", null, TipoCliente.STANDARD, true, 0);
        Solicitud solicitud = new Solicitud(null, clienteD, "Test", LocalDateTime.now(), Estado.ABIERTA, null, null);
        assertThrows(RuntimeException.class, () -> adapter.save(solicitud));
    }

    @Test
    void debe_retornar_vacio_si_la_solicitud_no_existe() {
        Optional<Solicitud> recuperada = adapter.findById(888L);
        assertTrue(recuperada.isEmpty());
    }
}