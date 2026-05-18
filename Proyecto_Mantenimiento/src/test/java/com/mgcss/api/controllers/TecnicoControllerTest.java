package com.mgcss.api.controllers;

import com.mgcss.api.dto.TecnicoRequestDTO;
import com.mgcss.api.dto.TecnicoResponseDTO;
import com.mgcss.api.mapper.TecnicoApiMapper;
import com.mgcss.domain.Tecnico;
import com.mgcss.services.TecnicoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(TecnicoController.class)
public class TecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TecnicoService tecnicoService;

    @Test
    void cuandoDesactivarTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).desactivarTecnico(1L);

        mockMvc.perform(put("/api/tecnicos/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoAsignarTareaATecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).asignarNuevaTarea(1L);

        mockMvc.perform(put("/api/tecnicos/1/asignar-tarea"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoFinalizarTareaDeTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).finalizarTarea(1L);

        mockMvc.perform(put("/api/tecnicos/1/finalizar-tarea"))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeMapearTecnicoAResponseDtoCorrectamente() {
        Tecnico tecnicoDominio = new Tecnico(1L, "Carlos", "Sistemas", true, 3);

        TecnicoResponseDTO responseDto = TecnicoApiMapper.toResponseDTO(tecnicoDominio);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Carlos", responseDto.getNombre());
        assertEquals("Sistemas", responseDto.getEspecialidad());
        assertTrue(responseDto.isActivo());
        assertEquals(3, responseDto.getCargaTrabajo());

        assertNull(TecnicoApiMapper.toResponseDTO(null));
    }

    @Test
    void debeManipularAtributosDeTecnicoRequestDto() {
        TecnicoRequestDTO requestDto = new TecnicoRequestDTO();
        requestDto.setNombre("Ana López");
        requestDto.setEspecialidad("FONTANERIA");

        assertEquals("Ana López", requestDto.getNombre());
        assertEquals("FONTANERIA", requestDto.getEspecialidad());

        TecnicoRequestDTO requestDtoConParametros = new TecnicoRequestDTO("Luis", "Redes");
        assertEquals("Luis", requestDtoConParametros.getNombre());
        assertEquals("Redes", requestDtoConParametros.getEspecialidad());
    }
}
