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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Pruebas unitarias para el controlador TecnicoController.
 * Valida la exposición de endpoints de la entidad Técnico, cubriendo flujos de éxito y excepciones.
 */
@WebMvcTest(TecnicoController.class)
public class TecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TecnicoService tecnicoService;

    // =========================================================================
    // TESTS: ENDPOINTS DEL CONTROLADOR
    // =========================================================================

    @Test
    void cuandoDesactivarTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).desactivarTecnico(1L);

        mockMvc.perform(put("/api/tecnicos/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoCrearTecnico_conDatosValidos_entoncesDevuelveCreatedYJson() throws Exception {
        // Arrange
        Tecnico tecnicoCreado = new Tecnico(1L, "Carlos Gomez", "Sistemas de Redes", true, 0);
        Mockito.when(tecnicoService.crearTecnico(anyString(), anyString())).thenReturn(tecnicoCreado);

        String jsonPayload = "{\"nombre\":\"Carlos Gomez\",\"especialidad\":\"Sistemas de Redes\"}";

        // Act & Assert
        mockMvc.perform(post("/api/tecnicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos Gomez"))
                .andExpect(jsonPath("$.especialidad").value("Sistemas de Redes"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.cargaTrabajo").value(0));
    }

    // Nuevo escenario de fallo: Intento de desactivación de un técnico que no existe
    @Test
    void cuandoDesactivarTecnicoInexistente_entoncesDevuelveNotFound() throws Exception {
        // Arrange: Configurar el simulacro para lanzar IllegalArgumentException ante un ID inválido
        Mockito.doThrow(new IllegalArgumentException("El técnico no existe"))
               .when(tecnicoService).desactivarTecnico(99L);

        // Act & Assert: Verificar que el GlobalExceptionHandler procesa la excepción devolviendo HTTP 404
        mockMvc.perform(put("/api/tecnicos/99/desactivar")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El técnico no existe"));
    }

    // Nuevo escenario de fallo: Intento de desactivación de un técnico con tareas asignadas activas (Invariante de dominio)
    @Test
    void cuandoDesactivarTecnicoConCargaActiva_entoncesDevuelveBadRequest() throws Exception {
        // Arrange: Configurar el simulacro para lanzar IllegalStateException debido a reglas de negocio
        Mockito.doThrow(new IllegalStateException("No se puede desactivar un técnico con solicitudes pendientes"))
               .when(tecnicoService).desactivarTecnico(2L);

        // Act & Assert: Verificar que el GlobalExceptionHandler procesa la excepción devolviendo HTTP 400
        mockMvc.perform(put("/api/tecnicos/2/desactivar")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se puede desactivar un técnico con solicitudes pendientes"));
    }

    // =========================================================================
    // TESTS: MAPPERS Y DTOS
    // =========================================================================

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
