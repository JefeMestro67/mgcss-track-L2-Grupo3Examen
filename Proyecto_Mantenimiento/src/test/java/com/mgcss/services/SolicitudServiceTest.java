package com.mgcss.services;

import com.mgcss.domain.*;
import com.mgcss.infrastructure.TecnicoRepository;
import com.mgcss.infrastructure.SolicitudRepository;

import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudServiceTest {

    // 1. Declaramos los mocks y el servicio a nivel de clase
    private SolicitudRepository mockRepoSolicitud;
    private TecnicoRepository mockRepoTecnico;
    private SolicitudService servicio;

    // 2. Lo configuramos UNA SOLA VEZ
    @BeforeEach
    void setUp() {
        mockRepoSolicitud = mock(SolicitudRepository.class);
        mockRepoTecnico = mock(TecnicoRepository.class);
        servicio = new SolicitudService(mockRepoSolicitud, mockRepoTecnico); 
    }

    @Test
    void debe_guardar_solicitud_al_asignar_tecnico() {
        // ARRANGE (Solo los datos específicos de este test)
        Solicitud solicitud = new Solicitud(1L, Estado.ABIERTA, null);
        Tecnico tecnico = new Tecnico(true);
        
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.of(tecnico));

        // ACT
        servicio.asignarTecnico(1L, 99L);

        // ASSERT
        verify(mockRepoSolicitud).save(solicitud);
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
    }
    
    @Test
    void debe_lanzar_excepcion_si_solicitud_no_existe() {
        // ARRANGE
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarTecnico(1L, 99L);
        });

        // REGLA DE ORO
        verify(mockRepoSolicitud, never()).save(any());
    }

    @Test
    void debe_guardar_solicitud_al_cerrarla() {
        // ARRANGE
        Solicitud solicitud = new Solicitud(1L, Estado.EN_PROCESO, null);
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));

        // ACT
        servicio.cerrarSolicitud(1L);

        // ASSERT
        verify(mockRepoSolicitud).save(solicitud);
        assertEquals(Estado.CERRADA, solicitud.getEstado());
    }

    @Test
    void debe_lanzar_excepcion_si_tecnico_no_existe() {
        // ARRANGE
        Solicitud solicitud = new Solicitud(1L, Estado.ABIERTA, null);
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarTecnico(1L, 99L);
        });

        // REGLA DE ORO
        verify(mockRepoSolicitud, never()).save(any());
    }
    
    @Test
    void debe_crear_y_guardar_una_solicitud_nueva() {
        // ARRANGE
        // Le decimos a Mockito: "Cuando alguien llame a save() con cualquier solicitud, devuelve esa misma solicitud"
        when(mockRepoSolicitud.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Solicitud creada = servicio.crearSolicitud();

        // ASSERT
        // 1. Verificamos que el servicio orquestó la llamada al repositorio
        verify(mockRepoSolicitud).save(any(Solicitud.class));
        
        // 2. Verificamos que la lógica de negocio básica se aplicó correctamente
        assertNotNull(creada, "La solicitud no debe ser nula");
        assertEquals(Estado.ABIERTA, creada.getEstado(), "Una solicitud nueva debe nacer en estado ABIERTA");
        assertNotNull(creada.getFechaCreacion(), "La solicitud debe tener una fecha de creación asignada");
    }
}