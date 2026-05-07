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
    void debe_incrementar_carga_cuando_tecnico_es_valido() {
        Tecnico tecnico = new Tecnico(1L, "Carlos Técnico", "Mantenimiento Hardware", true, 0);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnico));

        servicio.asignarNuevaTarea(1L);

        assertEquals(1, tecnico.getCargaTrabajo());
        assertEquals("Carlos Técnico", tecnico.getNombre()); 
        assertEquals("Mantenimiento Hardware", tecnico.getEspecialidad());
        verify(mockRepoTecnico).save(tecnico);
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
    void debe_desactivar_tecnico_si_no_tiene_carga() {
        Tecnico tecnicoLibre = new Tecnico(1L, "Ana", "Soporte", true, 0);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoLibre));

        servicio.desactivarTecnico(1L);

        assertFalse(tecnicoLibre.isActivo());
        verify(mockRepoTecnico).save(tecnicoLibre);
    }

    @Test
    void debe_finalizar_tarea_correctamente() {
        Tecnico tecnico = new Tecnico(1L, "Luis", "Soporte", true, 1);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnico));

        servicio.finalizarTarea(1L);

        assertEquals(0, tecnico.getCargaTrabajo());
        verify(mockRepoTecnico).save(tecnico);
    }

    @Test
    void debe_lanzar_excepcion_si_el_tecnico_no_existe_al_finalizar_tarea() {
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.finalizarTarea(1L);
        });
        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void no_debe_permitir_mas_de_cinco_tareas() {
        Tecnico tecnicoSaturado = new Tecnico(1L, "Luis", "Soporte", true, 5);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnicoSaturado));

        assertThrows(IllegalStateException.class, () -> {
            servicio.asignarNuevaTarea(1L);
        });
        verify(mockRepoTecnico, never()).save(any());
    }
    
    @Test
    void asignarNuevaTarea_Falla_Si_Tecnico_No_Existe() {
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.asignarNuevaTarea(1L);
        });
    }

    @Test
    void desactivarTecnico_Falla_Si_Tecnico_No_Existe() {
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.desactivarTecnico(1L);
        });
    }

    @Test
    void finalizarTarea_Falla_Si_Tecnico_No_Existe() {
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.finalizarTarea(1L);
        });
    }
}