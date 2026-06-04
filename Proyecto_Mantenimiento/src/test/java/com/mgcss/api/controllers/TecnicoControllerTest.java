package com.mgcss.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TecnicoController.class)
class TecnicoControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TecnicoService tecnicoService;

    @Test
    void cuandoCrearTecnico_entoncesDevuelveStatusCreatedYJson() throws Exception {
        TecnicoRequestDTO request = new TecnicoRequestDTO("Carlos", "Sistemas");
        Tecnico tecnicoMock = new Tecnico(1L, "Carlos", "Sistemas", true, 0);

        Mockito.when(tecnicoService.crearTecnico("Carlos", "Sistemas")).thenReturn(tecnicoMock);

        mockMvc.perform(post("/api/tecnicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.especialidad").value("Sistemas"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.cargaTrabajo").value(0));
    }

    @Test
    void cuandoDesactivarTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).desactivarTecnico(1L);
        
        mockMvc.perform(put("/api/tecnicos/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoDesactivarTecnicoInexistente_entoncesDevuelveNotFound() throws Exception {
        // Arrange: Simulamos que el servicio lanza IllegalArgumentException si el ID no existe
        Mockito.doThrow(new IllegalArgumentException("El técnico no existe"))
               .when(tecnicoService).desactivarTecnico(99L);

        // Act & Assert: Validamos que el GlobalExceptionHandler responda con un 404 en el body JSON
        mockMvc.perform(put("/api/tecnicos/99/desactivar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El técnico no existe"));
    }

    @Test
    void cuandoDesactivarTecnicoConCarga_entoncesDevuelveBadRequest() throws Exception {
        // Arrange: Simulamos que se viola el invariante lanzando IllegalStateException
        Mockito.doThrow(new IllegalStateException("No se puede desactivar un técnico con tareas pendientes"))
               .when(tecnicoService).desactivarTecnico(1L);

        // Act & Assert: El handler reconduce el fallo a un 400 Bad Request
        mockMvc.perform(put("/api/tecnicos/1/desactivar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se puede desactivar un técnico con tareas pendientes"));
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