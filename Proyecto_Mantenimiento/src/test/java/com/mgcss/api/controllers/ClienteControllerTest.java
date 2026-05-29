package com.mgcss.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgcss.api.dto.ClienteRequestDTO;
import com.mgcss.domain.Cliente;
import com.mgcss.domain.TipoCliente;
import com.mgcss.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente(1L, "Carlos Gomez", "carlos@mgcss.com", TipoCliente.STANDARD, true, 0);
    }

    @Test
    void cuandoCrearCliente_entoncesDevuelveStatusCreatedYJson() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO("Carlos Gomez", "carlos@mgcss.com");

        Mockito.when(clienteService.crearCliente("Carlos Gomez", "carlos@mgcss.com"))
               .thenReturn(clienteMock);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Carlos Gomez"))
                .andExpect(jsonPath("$.email").value("carlos@mgcss.com"));
    }

    @Test
    void cuandoDesactivarCliente_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(clienteService).desactivarCliente(1L);

        mockMvc.perform(put("/api/clientes/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoDesactivarClienteInexistente_entoncesDevuelveNotFound() throws Exception {
        // Arrange: Simulamos que el servicio lanza IllegalArgumentException si el cliente no existe
        Mockito.doThrow(new IllegalArgumentException("El cliente no existe"))
               .when(clienteService).desactivarCliente(99L);

        // Act & Assert: Validamos que el GlobalExceptionHandler responda con un 404
        mockMvc.perform(put("/api/clientes/99/desactivar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El cliente no existe"));
    }

    @Test
    void cuandoDesactivarClienteConSolicitudesAbiertas_entoncesDevuelveBadRequest() throws Exception {
        // Arrange: Simulamos la violación de la regla de negocio del dominio (IllegalStateException)
        Mockito.doThrow(new IllegalStateException("No se puede desactivar un cliente con solicitudes abiertas"))
               .when(clienteService).desactivarCliente(1L);

        // Act & Assert: Validamos que transicione a un HTTP 400 Bad Request limpio gracias al handler
        mockMvc.perform(put("/api/clientes/1/desactivar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se puede desactivar un cliente con solicitudes abiertas"));
    }
}