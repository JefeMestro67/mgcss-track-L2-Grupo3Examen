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
    void debe_crear_solicitud_y_asociarla_al_cliente_incrementando_carga() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, true, 0);
        
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));
        when(mockRepoSolicitud.save(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud creada = servicio.crearSolicitud(1L, "Mi PC no arranca");

        assertNotNull(creada);
        assertEquals(cliente, creada.getCliente());
        assertEquals("Mi PC no arranca", creada.getDescripcion());
        assertEquals(1, cliente.getSolicitudesAbiertas()); 

        verify(mockRepoCliente).save(cliente);
        verify(mockRepoSolicitud).save(any(Solicitud.class));
    }

    @Test
    void asignarTecnico_debe_incrementar_carga_del_tecnico() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnico = new Tecnico(99L, "Carlos", "Sistemas", true, 0);

        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.of(tecnico));

        servicio.asignarTecnico(1L, 99L);

        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
        assertEquals(tecnico, solicitud.getTecnicoAsignado());
        assertEquals(1, tecnico.getCargaTrabajo()); 

        verify(mockRepoTecnico).save(tecnico);
        verify(mockRepoSolicitud).save(solicitud);
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

        verify(mockRepoCliente).save(cliente);
        verify(mockRepoTecnico).save(tecnico);
        verify(mockRepoSolicitud).save(solicitud);
    }

    @Test
    void crearSolicitud_falla_si_cliente_no_existe() {
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> servicio.crearSolicitud(1L, "Error"));
        verify(mockRepoSolicitud, never()).save(any());
    }

    @Test
    void asignarTecnico_falla_y_no_guarda_si_tecnico_inactivo() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnicoInactivo = new Tecnico(99L, "Inactivo", "Sistemas", false, 0);

        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.of(tecnicoInactivo));

        assertThrows(IllegalStateException.class, () -> servicio.asignarTecnico(1L, 99L));

        verify(mockRepoSolicitud, never()).save(any());
        verify(mockRepoTecnico, never()).save(any());
    }
    
    @Test
    void asignarTecnico_Falla_Cuando_Solicitud_No_Existe_Explicito() {
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarTecnico(1L, 99L);
        });
        verify(mockRepoTecnico, never()).findById(anyLong());
        verify(mockRepoSolicitud, never()).save(any());
    }

    @Test
    void asignarTecnico_Falla_Cuando_Tecnico_No_Existe_Explicito() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarTecnico(1L, 99L);
        });
        verify(mockRepoSolicitud, never()).save(any());
    }

    @Test
    void cerrarSolicitud_Falla_Si_No_Existe() {
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.cerrarSolicitud(1L);
        });
    }
}