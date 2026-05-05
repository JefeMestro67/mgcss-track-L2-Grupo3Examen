package com.mgcss.services;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.NoSuchElementException;

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
    void debe_incrementar_carga_cuando_tecnico_es_valido() {
        // ARRANGE: Técnico activo con carga 0 (usando constructor de sobrecarga si lo añadiste)
        Tecnico tecnico = new Tecnico(1L, "Carlos", true, 0);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnico));

        // ACT
        servicio.asignarNuevaTarea(1L);

        // ASSERT
        assertEquals(1, tecnico.getCargaTrabajo(), "La carga debería haber subido a 1");
        verify(mockRepoTecnico).save(tecnico);
    }

    @Test
    void no_debe_desactivar_tecnico_si_tiene_carga_pendiente() {
        // ARRANGE: Técnico con carga 2 (Regla 1)
        Tecnico tecnicoConCarga = new Tecnico(1L, "Ana", true, 2);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoConCarga));

        // ACT & ASSERT
        assertThrows(IllegalStateException.class, () -> {
            servicio.desactivarTecnico(1L);
        });

        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void debe_desactivar_tecnico_si_no_tiene_carga() {
        // ARRANGE
        Tecnico tecnicoLibre = new Tecnico(1L, "Ana", true, 0);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoLibre));

        // ACT
        servicio.desactivarTecnico(1L);

        // ASSERT
        assertFalse(tecnicoLibre.isActivo());
        verify(mockRepoTecnico).save(tecnicoLibre);
    }

    @Test
    void debe_lanzar_excepcion_si_el_tecnico_no_existe() {
        // ARRANGE
        when(mockRepoTecnico.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT: Cambiado de NoSuchElementException a IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarNuevaTarea(99L);
        });

        // Verificamos que nunca se intentó guardar nada
        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void no_debe_permitir_mas_de_cinco_tareas() {
        // ARRANGE: Técnico al límite (Regla 2)
        Tecnico tecnicoSaturado = new Tecnico(1L, "Luis", true, 5);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoSaturado));

        // ACT & ASSERT
        assertThrows(IllegalStateException.class, () -> {
            servicio.asignarNuevaTarea(1L);
        });
        
        verify(mockRepoTecnico, never()).save(any());
    }
}
