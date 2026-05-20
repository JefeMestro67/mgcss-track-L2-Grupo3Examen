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
}