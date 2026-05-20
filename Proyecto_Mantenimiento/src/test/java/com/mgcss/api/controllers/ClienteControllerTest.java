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

/**
 * Pruebas unitarias para el controlador ClienteController.
 * Verifica la correcta exposición de los endpoints y el manejo de respuestas HTTP de éxito y fallo.
 */
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        // Usamos el constructor exacto: (id, nombre, email, tipoCliente, activo, solicitudesAbiertas)
        clienteMock = new Cliente(1L, "Carlos Gomez", "carlos@mgcss.com", TipoCliente.STANDARD, true, 0);
    }

    // 1. Test de POST → Crear Cliente
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

    // 2. Test de PUT → Desactivar Cliente
    @Test
    void cuandoDesactivarCliente_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(clienteService).desactivarCliente(1L);

        mockMvc.perform(put("/api/clientes/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    // 3. Test de Escenario de Fallo → Intentar operar con un Cliente Inexistente
    @Test
    void cuandoDesactivarClienteInexistente_entoncesDevuelveNotFound() throws Exception {
        // Arrange: Se configura el simulacro para lanzar la excepción cuando no exista el cliente
        Mockito.doThrow(new IllegalArgumentException("El cliente no existe"))
               .when(clienteService).desactivarCliente(99L);

        // Act & Assert: Se valida que el GlobalExceptionHandler intercepte la excepción y responda HTTP 404
        mockMvc.perform(put("/api/clientes/99/desactivar")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El cliente no existe"));
    }
}
