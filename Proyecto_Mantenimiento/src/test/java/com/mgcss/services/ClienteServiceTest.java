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
    void debe_crear_solicitud_para_cliente() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(1);
        
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        // ACT
        servicio.crearSolicitud(1L);

        // ASSERT
        verify(mockRepoCliente).save(cliente);
        assertEquals(2, cliente.getSolicitudesAbiertas());
    }

    @Test
    void no_debe_permitir_crear_solicitud_si_cliente_no_existe() {
        // ARRANGE
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.crearSolicitud(1L);
        });

        // REGLA DE ORO
        verify(mockRepoCliente, never()).save(any());
    }

    @Test
    void debe_desactivar_cliente_si_no_tiene_solicitudes_abiertas() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(0);
        
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        // ACT
        servicio.desactivarCliente(1L);

        // ASSERT
        assertFalse(cliente.isActivo());
        verify(mockRepoCliente).save(cliente);
    }

    @Test
    void no_debe_desactivar_cliente_si_tiene_solicitudes_abiertas() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(1);
        
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        // ACT & ASSERT
        assertThrows(IllegalStateException.class, () -> {
            servicio.desactivarCliente(1L);
        });

        // REGLA DE ORO
        verify(mockRepoCliente, never()).save(any());
    }

    @Test
    void debe_finalizar_solicitud_para_cliente() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(2);
        
        when(mockRepoCliente.findById(1L)).thenReturn(Optional.of(cliente));

        // ACT
        servicio.finalizarSolicitud(1L);

        // ASSERT
        assertEquals(1, cliente.getSolicitudesAbiertas());
        verify(mockRepoCliente).save(cliente);
    }

    @Test
    void debe_crear_y_guardar_un_cliente_nuevo() {
        // ARRANGE
        when(mockRepoCliente.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Cliente creado = servicio.crearCliente("Juan Pérez", "juan.perez@example.com");

        // ASSERT
        verify(mockRepoCliente).save(any(Cliente.class));
        assertNotNull(creado);
        assertEquals("Juan Pérez", creado.getNombre());
        assertEquals("juan.perez@example.com", creado.getEmail());
        assertTrue(creado.isActivo());
        assertEquals(0, creado.getSolicitudesAbiertas());
    }
}
