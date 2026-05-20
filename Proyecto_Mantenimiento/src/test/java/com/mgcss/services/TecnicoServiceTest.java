package com.mgcss.services;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TecnicoServiceTest {

    private TecnicoRepository mockRepoTecnico;
    private TecnicoService servicio;

    @BeforeEach
    void setUp() {
        mockRepoTecnico = mock(TecnicoRepository.class);
        servicio = new TecnicoService(mockRepoTecnico);
    }

    @Test
    void debe_desactivar_tecnico_si_no_tiene_carga() {
        Tecnico tecnicoLibre = new Tecnico(1L, "Ana", "Soporte", true, 0);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoLibre));

        servicio.desactivarTecnico(1L);

        assertFalse(tecnicoLibre.isActivo());
        verify(mockRepoTecnico).save(tecnicoLibre);
    }

    @Test
    void no_debe_desactivar_tecnico_si_tiene_carga_pendiente() {
        Tecnico tecnicoConCarga = new Tecnico(1L, "Ana", "Soporte", true, 2);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoConCarga));

        assertThrows(IllegalStateException.class, () -> {
            servicio.desactivarTecnico(1L);
        });
        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void desactivarTecnico_Falla_Si_Tecnico_No_Existe() {
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.desactivarTecnico(1L);
        });
        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void debe_crear_tecnico_exitosamente_cuando_los_datos_son_validos() {
        String nombre = "Carlos Gomez";
        String especialidad = "Sistemas de Redes";
        
        when(mockRepoTecnico.save(any(Tecnico.class))).thenAnswer(invocation -> {
            Tecnico t = invocation.getArgument(0);
            return new Tecnico(1L, t.getNombre(), t.getEspecialidad(), t.isActivo(), t.getCargaTrabajo());
        });

        Tecnico resultado = servicio.crearTecnico(nombre, especialidad);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(nombre, resultado.getNombre());
        assertEquals(especialidad, resultado.getEspecialidad());
        assertTrue(resultado.isActivo());
        assertEquals(0, resultado.getCargaTrabajo());
        
        verify(mockRepoTecnico, times(1)).save(any(Tecnico.class));
    }

    @Test
    void debe_lanzar_excepcion_cuando_el_nombre_es_nulo_o_vacio() {
        IllegalArgumentException exVacio = assertThrows(IllegalArgumentException.class, () -> {
            servicio.crearTecnico("   ", "Soporte");
        });
        assertEquals("El nombre del técnico es obligatorio", exVacio.getMessage());

        IllegalArgumentException exNulo = assertThrows(IllegalArgumentException.class, () -> {
            servicio.crearTecnico(null, "Soporte");
        });
        assertEquals("El nombre del técnico es obligatorio", exNulo.getMessage());
        
        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void debe_lanzar_excepcion_cuando_la_especialidad_es_nulo_o_vacio() {
        IllegalArgumentException exVacio = assertThrows(IllegalArgumentException.class, () -> {
            servicio.crearTecnico("Carlos", "");
        });
        assertEquals("La especialidad del técnico es obligatoria", exVacio.getMessage());

        IllegalArgumentException exNulo = assertThrows(IllegalArgumentException.class, () -> {
            servicio.crearTecnico("Carlos", null);
        });
        assertEquals("La especialidad del técnico es obligatoria", exNulo.getMessage());
        
        verify(mockRepoTecnico, never()).save(any());
    }
}