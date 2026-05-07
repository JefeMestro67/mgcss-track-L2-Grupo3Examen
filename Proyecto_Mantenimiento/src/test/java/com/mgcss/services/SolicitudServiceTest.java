package com.mgcss.services;

import com.mgcss.domain.*;
import com.mgcss.infrastructure.ClienteRepository;
import com.mgcss.infrastructure.TecnicoRepository;
import com.mgcss.infrastructure.SolicitudRepository;

import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudServiceTest {

    private SolicitudRepository mockRepoSolicitud;
    private TecnicoRepository mockRepoTecnico;
    private ClienteRepository mockRepoCliente;
    private SolicitudService servicio;

    @BeforeEach
    void setUp() {
        mockRepoSolicitud = mock(SolicitudRepository.class);
        mockRepoTecnico = mock(TecnicoRepository.class);
        mockRepoCliente = mock(ClienteRepository.class);
        servicio = new SolicitudService(mockRepoSolicitud, mockRepoTecnico, mockRepoCliente); 
    }

    @Test
    void reabrirSolicitud_debe_actualizar_estado_y_cargas() {
        // Setup: Solicitud CERRADA con cliente y técnico
        Cliente cliente = new Cliente(1L, "Juan", "j@t.com", TipoCliente.STANDARD, true, 1);
        Tecnico tecnico = new Tecnico(2L, "Carlos", "IT", true, 1);
        Solicitud solicitud = new Solicitud(10L, cliente, "Desc", LocalDateTime.now(), Estado.CERRADA, tecnico, LocalDateTime.now());

        when(mockRepoSolicitud.findById(10L)).thenReturn(Optional.of(solicitud));

        servicio.reabrirSolicitud(10L);

        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
        assertNull(solicitud.getFechaCierre());
        assertEquals(2, cliente.getSolicitudesAbiertas()); // Incrementa al reabrir
        assertEquals(2, tecnico.getCargaTrabajo());        // Incrementa al reabrir

        verify(mockRepoSolicitud).save(solicitud);
        verify(mockRepoCliente).save(cliente);
        verify(mockRepoTecnico).save(tecnico);
    }

    @Test
    void reabrirSolicitud_falla_si_no_existe() {
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> servicio.reabrirSolicitud(1L));
    }

    // ... (Mantén aquí el resto de tus tests: crearSolicitud, asignarTecnico, cerrarSolicitud...)
    // Asegúrate de incluir todos los que ya tenías para no perder coverage antiguo.
    
    @Test
    void debe_crear_solicitud_y_asociarla_al_cliente_incrementando_carga() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, true, 0);
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));
        when(mockRepoSolicitud.save(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));
        Solicitud creada = servicio.crearSolicitud(1L, "Mi PC no arranca");
        assertNotNull(creada);
        assertEquals(1, cliente.getSolicitudesAbiertas());
    }

    @Test
    void asignarTecnico_debe_incrementar_carga_del_tecnico() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnico = new Tecnico(99L, "Carlos", "Sistemas", true, 0);
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.of(tecnico));
        servicio.asignarTecnico(1L, 99L);
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
        assertEquals(1, tecnico.getCargaTrabajo());
    }

    @Test
    void cerrarSolicitud_debe_liberar_carga_de_cliente_y_tecnico() {
        Cliente cliente = new Cliente(10L, "Juan", "j@test.com", TipoCliente.STANDARD, true, 2);
        Tecnico tecnico = new Tecnico(99L, "Carlos", "Sistemas", true, 3);
        Solicitud solicitud = new Solicitud(1L, cliente, "Desc", LocalDateTime.now(), Estado.EN_PROCESO, tecnico, null);
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        servicio.cerrarSolicitud(1L);
        assertEquals(Estado.CERRADA, solicitud.getEstado());
        assertEquals(1, cliente.getSolicitudesAbiertas());
        assertEquals(2, tecnico.getCargaTrabajo());
    }
}