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
}