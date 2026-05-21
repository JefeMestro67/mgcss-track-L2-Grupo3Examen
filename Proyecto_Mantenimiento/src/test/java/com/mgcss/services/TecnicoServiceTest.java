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
    void no_debe_desactivar_tecnico_si_tiene_carga_pendiente() {
        Tecnico tecnico = new Tecnico(1L, "Ana", "Sistemas", true, 1);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnico));

        assertThrows(IllegalStateException.class, () -> {
            servicio.desactivarTecnico(1L);
        });
        verify(mockRepoTecnico, never()).save(any());
    }

    @Test
    void debe_desactivar_tecnico_sin_carga() {
        Tecnico tecnico = new Tecnico(1L, "Ana", "Sistemas", true, 0);
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.of(tecnico));

        servicio.desactivarTecnico(1L);

        assertFalse(tecnico.isActivo());
        verify(mockRepoTecnico).save(tecnico);
    }

    @Test
    void desactivarTecnico_Falla_Si_Tecnico_No_Existe() {
        when(mockRepoTecnico.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.desactivarTecnico(1L);
        });
    }
}