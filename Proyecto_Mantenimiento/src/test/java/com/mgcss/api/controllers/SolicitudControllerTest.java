package com.mgcss.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgcss.api.dto.EstadoChangeDTO;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.domain.*;
import com.mgcss.services.SolicitudService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolicitudService solicitudService;

    private Cliente clienteMock;
    private Solicitud solicitudMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente(1L, "Juan Perez", "juan@mgcss.com", TipoCliente.STANDARD, true, 0);
        solicitudMock = new Solicitud(1L, clienteMock, "Error en el servidor", LocalDateTime.of(2026, 5, 18, 12, 0), Estado.ABIERTA, null, null);
    }

    @Test
    void cuandoCrearSolicitud_entoncesDevuelveStatusCreatedYJson() throws Exception {
        SolicitudRequestDTO request = new SolicitudRequestDTO(1L, "Error en el servidor");
        Mockito.when(solicitudService.crearSolicitud(1L, "Error en el servidor")).thenReturn(solicitudMock);

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Error en el servidor"))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void cuandoConsultarPorIdExistente_entoncesDevuelveStatusOK() throws Exception {
        Mockito.when(solicitudService.buscarPorId(1L)).thenReturn(solicitudMock);
        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void cuandoConsultarPorIdInexistente_entoncesDevuelveBadRequest() { // SonarCloud: throws Exception eliminado
        Mockito.when(solicitudService.buscarPorId(99L)).thenThrow(new IllegalArgumentException("La solicitud no existe"));
        Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/solicitudes/99"));
        });
    }

    @Test
    void cuandoAsignarTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(solicitudService).asignarTecnico(1L, 2L);
        mockMvc.perform(put("/api/solicitudes/1/tecnico").param("tecnicoId", "2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoCerrarSolicitud_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(solicitudService).cerrarSolicitud(1L);
        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoReabrirSolicitud_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(solicitudService).reabrirSolicitud(1L);
        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoListarSolicitudes_entoncesDevuelveListaYStatusOk() throws Exception {
        Mockito.when(solicitudService.listarTodas()).thenReturn(Collections.singletonList(solicitudMock));
        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void debeMapearEstadoChangeDtoCorrectamente() {
        LocalDateTime fecha = LocalDateTime.now();
        EstadoChangeDTO dto = new EstadoChangeDTO(Estado.ABIERTA, Estado.EN_PROCESO, fecha);
        Assertions.assertEquals(Estado.ABIERTA, dto.getEstadoAnterior());
        Assertions.assertEquals(Estado.EN_PROCESO, dto.getEstadoNuevo());
        Assertions.assertEquals(fecha, dto.getFechaCambio());
    }
}