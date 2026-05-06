package com.mgcss.services;

import com.mgcss.domain.*;
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
    private SolicitudService servicio;

    @BeforeEach
    void setUp() {
        mockRepoSolicitud = mock(SolicitudRepository.class);
        mockRepoTecnico = mock(TecnicoRepository.class);
        servicio = new SolicitudService(mockRepoSolicitud, mockRepoTecnico); 
    }

    @Test
    void debe_guardar_solicitud_al_asignar_tecnico() {
        // ARRANGE - Rellenamos todos los campos para cubrir getters/setters de Solicitud y Tecnico
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setDescripcion("Error en el sistema de login");
        solicitud.setEstado(Estado.ABIERTA);
        solicitud.setFechaCreacion(LocalDateTime.now());

        Tecnico tecnico = new Tecnico();
        tecnico.setId(99L);
        tecnico.setNombre("Carlos Técnico");
        tecnico.setEspecialidad("Sistemas");
        tecnico.setActivo(true);
        tecnico.setCargaTrabajo(0);
        
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.of(tecnico));

        // ACT
        servicio.asignarTecnico(1L, 99L);

        // ASSERT
        verify(mockRepoSolicitud).save(solicitud);
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
        assertEquals(tecnico, solicitud.getTecnicoAsignado()); // Cubre el getter de tecnicoAsignado
        assertEquals("Carlos Técnico", solicitud.getTecnicoAsignado().getNombre()); // Cubre getter de nombre
    }
    
    @Test
    void debe_lanzar_excepcion_si_solicitud_no_existe() {
        // ARRANGE
        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarTecnico(1L, 99L);
        });

        verify(mockRepoSolicitud, never()).save(any());
    }

    @Test
    void debe_guardar_solicitud_al_cerrarla() {
        // ARRANGE - Datos completos
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setDescripcion("PC no arranca");
        solicitud.setEstado(Estado.EN_PROCESO);
        solicitud.setFechaCreacion(LocalDateTime.now().minusDays(1));

        when(mockRepoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));

        // ACT
        servicio.cerrarSolicitud(1L);

        // ASSERT
        verify(mockRepoSolicitud).save(solicitud);
        assertEquals(Estado.CERRADA, solicitud.getEstado());
        assertNotNull(solicitud.getFechaCierre(), "La fecha de cierre debe haberse generado"); // Cubre el setter/getter de fechaCierre
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

        verify(mockRepoSolicitud, never()).save(any());
    }
    
    @Test
    void debe_crear_y_guardar_una_solicitud_nueva() {
        // ARRANGE - Usamos answer para que el mock devuelva la solicitud que recibe
        when(mockRepoSolicitud.save(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud s = invocation.getArgument(0);
            s.setId(500L); // Simulamos que la DB le da un ID
            return s;
        });

        // ACT
        Solicitud creada = servicio.crearSolicitud();

        // ASSERT
        verify(mockRepoSolicitud).save(any(Solicitud.class));
        
        assertNotNull(creada, "La solicitud no debe ser nula");
        assertEquals(500L, creada.getId()); // Cubre getId
        assertEquals(Estado.ABIERTA, creada.getEstado());
        assertNotNull(creada.getFechaCreacion());
    }
}