package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteTest {

    // --- REGLA 1: DESACTIVAR CLIENTE ---

    @Test
    void no_debe_permitir_desactivar_cliente_si_tiene_solicitudes_abiertas() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(1);
        
        assertThrows(IllegalStateException.class, cliente::desactivar);
    }

    @Test
    void debe_permitir_desactivar_cliente_si_no_tiene_solicitudes_abiertas() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(0);
        
        cliente.desactivar();
        assertFalse(cliente.isActivo());
    }

    // --- REGLA 2: LÍMITE DE SOLICITUDES ---

    @Test
    void no_debe_permitir_crear_solicitud_si_llega_al_limite() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(3);
        
        assertThrows(IllegalStateException.class, cliente::crearSolicitud);
    }

    @Test
    void no_debe_permitir_crear_solicitud_si_esta_inactivo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setActivo(false);
        cliente.setSolicitudesAbiertas(0);
        
        assertThrows(IllegalStateException.class, cliente::crearSolicitud);
    }
    
    @Test
    void debe_permitir_crear_solicitud_si_esta_activo_y_por_debajo_del_limite() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(1);
        
        cliente.crearSolicitud();
        assertEquals(2, cliente.getSolicitudesAbiertas());
    }

    // --- REGLA 3: FINALIZAR SOLICITUD ---

    @Test
    void debe_reducir_solicitudes_abiertas_al_finalizar_solicitud() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(2);
        
        cliente.finalizarSolicitud();
        assertEquals(1, cliente.getSolicitudesAbiertas());
    }

    @Test
    void no_debe_permitir_finalizar_solicitud_si_el_numero_ya_es_cero() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(0);
        
        assertThrows(IllegalStateException.class, cliente::finalizarSolicitud);
    }
    
    @Test
    void debe_obtener_correctamente_los_atributos_del_cliente() {
        Cliente cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNombre("Roberto");
        cliente.setEmail("roberto@example.com");
        cliente.setTipoCliente(TipoCliente.PREMIUM); // Usamos un valor válido del enum
        
        assertEquals(10L, cliente.getId());
        assertEquals("Roberto", cliente.getNombre());
        assertEquals("roberto@example.com", cliente.getEmail());
        assertEquals(TipoCliente.PREMIUM, cliente.getTipoCliente());
    }
}
