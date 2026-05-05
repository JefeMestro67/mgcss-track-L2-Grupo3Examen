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

    private SolicitudRepository mockRepoSolicitud;
    private TecnicoRepository mockRepoTecnico;
    private SolicitudService servicio;

    @BeforeEach
    void setUp() {
        mockRepoSolicitud = mock(SolicitudRepository.class);
        mockRepoTecnico = mock(TecnicoRepository.class);
        servicio = new SolicitudService(mockRepoSolicitud, mockRepoTecnico); 
    }

    @Test
    void debe_guardar_solicitud_al_asignar_tecnico() {
        // ARRANGE
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);

        Tecnico tecnico = new Tecnico();
        tecnico.setActivo(true);
        
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
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.EN_PROCESO);

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
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);

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
        when(mockRepoSolicitud.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Solicitud creada = servicio.crearSolicitud();

        // ASSERT
        verify(mockRepoSolicitud).save(any(Solicitud.class));
        
        assertNotNull(creada, "La solicitud no debe ser nula");
        assertEquals(Estado.ABIERTA, creada.getEstado(), "Una solicitud nueva debe nacer en estado ABIERTA");
        assertNotNull(creada.getFechaCreacion(), "La solicitud debe tener una fecha de creación asignada");
    }
}