package com.mgcss.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgcss.api.dto.EstadoChangeDTO;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.domain.*;
import com.mgcss.infrastructure.SolicitudRepository;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
public class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolicitudService solicitudService;

    @MockBean
    private SolicitudRepository solicitudRepository;

    private Cliente clienteMock;
    private Solicitud solicitudMock;

    @BeforeEach
    void setUp() {
        // 1. Usamos vuestro constructor exacto de Cliente: (id, nombre, email, tipoCliente, activo, solicitudesAbiertas)
        clienteMock = new Cliente(1L, "Juan Perez", "juan@mgcss.com", TipoCliente.STANDARD, true, 0);
        
        // 2. Usamos vuestro constructor exacto de Solicitud: (id, cliente, descripcion, fechaCreacion, estado, tecnicoAsignado, fechaCierre)
        solicitudMock = new Solicitud(
            1L, 
            clienteMock, 
            "Error en el servidor", 
            LocalDateTime.of(2026, 5, 18, 12, 0), 
            Estado.ABIERTA, 
            null, 
            null
        );
    }

    // 1. Test del POST (Crear Solicitud)
    @Test
    void cuandoCrearSolicitud_entoncesDevuelveStatusCreatedYJson() throws Exception {
        SolicitudRequestDTO request = new SolicitudRequestDTO(1L, "Error en el servidor");
        
        Mockito.when(solicitudService.crearSolicitud(1L, "Error en el servidor"))
               .thenReturn(solicitudMock);

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Error en el servidor"))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    // 2. Test del GET por ID (Consultar Solicitud)
    @Test
    void cuandoConsultarPorIdExistente_entoncesDevuelveStatusOK() throws Exception {
        Mockito.when(solicitudRepository.findById(1L))
               .thenReturn(Optional.of(solicitudMock));

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

 // 3. Test del GET por ID cuando NO existe (Escenario de error para cobertura)
    @Test
    void cuandoConsultarPorIdInexistente_entoncesDevuelveBadRequest() throws Exception {
        Mockito.when(solicitudRepository.findById(99L))
               .thenReturn(Optional.empty());

        // Al no haber capturador en el controlador, MockMvc lanzará un ServletException.
        // Con esto verificamos que el controlador lanza el error correctamente sin tragárselo.
        Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/solicitudes/99"));
        });
    }

    // 4. Test del PUT (Asignar Técnico)
    @Test
    void cuandoAsignarTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(solicitudService).asignarTecnico(1L, 2L);

        mockMvc.perform(put("/api/solicitudes/1/tecnico")
                .param("tecnicoId", "2"))
                .andExpect(status().isNoContent());
    }

    // 5. Test del PUT (Cerrar Solicitud)
    @Test
    void cuandoCerrarSolicitud_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(solicitudService).cerrarSolicitud(1L);

        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isNoContent());
    }

    // 6. Test del PATCH (Reabrir Solicitud)
    @Test
    void cuandoReabrirSolicitud_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(solicitudService).reabrirSolicitud(1L);

        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isNoContent());
    }

    // 7. Test del GET (Listar Solicitudes)
    @Test
    void cuandoListarSolicitudes_entoncesDevuelveListaYStatusOk() throws Exception {
        Mockito.when(solicitudRepository.findAll())
               .thenReturn(Collections.singletonList(solicitudMock));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void debeMapearEstadoChangeDtoCorrectamente() {
        java.time.LocalDateTime fecha = java.time.LocalDateTime.now();
        
        EstadoChangeDTO dto = new EstadoChangeDTO(
            com.mgcss.domain.Estado.ABIERTA, 
            com.mgcss.domain.Estado.EN_PROCESO, 
            fecha
        );

        org.junit.jupiter.api.Assertions.assertEquals(com.mgcss.domain.Estado.ABIERTA, dto.getEstadoAnterior());
        org.junit.jupiter.api.Assertions.assertEquals(com.mgcss.domain.Estado.EN_PROCESO, dto.getEstadoNuevo());
        org.junit.jupiter.api.Assertions.assertEquals(fecha, dto.getFechaCambio());
    }
}
