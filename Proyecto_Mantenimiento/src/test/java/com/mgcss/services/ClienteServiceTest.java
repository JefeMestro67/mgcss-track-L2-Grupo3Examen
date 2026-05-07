package com.mgcss.services;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.TipoCliente;
import com.mgcss.infrastructure.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteRepository mockRepoCliente;
    private ClienteService servicio;

    @BeforeEach
    void setUp() {
        mockRepoCliente = mock(ClienteRepository.class);
        servicio = new ClienteService(mockRepoCliente);
    }

    @Test
    void debe_desactivar_cliente_si_no_tiene_solicitudes_abiertas() {
        Cliente cliente = new Cliente(1L, "Juan Pérez", "juan@example.com", TipoCliente.STANDARD, true, 0);
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        servicio.desactivarCliente(1L);

        assertFalse(cliente.isActivo());
        verify(mockRepoCliente).save(cliente);
    }

    @Test
    void no_debe_desactivar_cliente_si_tiene_solicitudes_abiertas() {
        Cliente cliente = new Cliente(1L, "Juan Pérez", "juan@example.com", TipoCliente.STANDARD, true, 1);
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalStateException.class, () -> {
            servicio.desactivarCliente(1L);
        });
        verify(mockRepoCliente, never()).save(any());
    }

    @Test
    void no_debe_desactivar_cliente_si_no_existe() {
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.desactivarCliente(1L);
        });
    }

    @Test
    void debe_finalizar_solicitud_para_cliente() {
        Cliente cliente = new Cliente(1L, "Juan Pérez", "juan@example.com", TipoCliente.STANDARD, true, 2);
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        servicio.finalizarSolicitud(1L);

        assertEquals(1, cliente.getSolicitudesAbiertas());
        verify(mockRepoCliente).save(cliente);
    }

    @Test
    void no_debe_finalizar_solicitud_si_cliente_no_existe() {
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.finalizarSolicitud(1L);
        });
    }

    @Test
    void debe_crear_y_guardar_un_cliente_nuevo() {
        when(mockRepoCliente.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente creado = servicio.crearCliente("Juan Pérez", "juan.perez@example.com");

        verify(mockRepoCliente).save(any(Cliente.class));
        assertNotNull(creado);
        assertEquals("Juan Pérez", creado.getNombre());
        assertEquals("juan.perez@example.com", creado.getEmail());
        assertTrue(creado.isActivo());
        assertEquals(0, creado.getSolicitudesAbiertas());
    }
}