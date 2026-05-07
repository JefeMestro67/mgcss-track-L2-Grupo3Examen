package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ClienteTest {

    @Test
    void no_debe_permitir_desactivar_cliente_si_tiene_solicitudes_abiertas() {
        Cliente cliente = new Cliente(1L, "Juan Pérez", "juan@example.com", TipoCliente.STANDARD, true, 1);
        assertThrows(IllegalStateException.class, cliente::desactivar);
    }

    @Test
    void debe_permitir_desactivar_cliente_si_no_tiene_solicitudes_abiertas() {
        Cliente cliente = new Cliente(1L, "Juan Pérez", "juan@example.com", TipoCliente.STANDARD, true, 0);
        cliente.desactivar();
        assertFalse(cliente.isActivo());
    }

    @Test
    void no_debe_permitir_crear_solicitud_si_llega_al_limite() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, true, 3);
        assertThrows(IllegalStateException.class, cliente::crearSolicitud);
    }

    @Test
    void no_debe_permitir_crear_solicitud_si_esta_inactivo() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, false, 0);
        assertThrows(IllegalStateException.class, cliente::crearSolicitud);
    }
    
    @Test
    void debe_permitir_crear_solicitud_si_esta_activo_y_por_debajo_del_limite() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, true, 1);
        cliente.crearSolicitud();
        assertEquals(2, cliente.getSolicitudesAbiertas());
    }

    @Test
    void debe_reducir_solicitudes_abiertas_al_finalizar_solicitud() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, true, 2);
        cliente.finalizarSolicitud();
        assertEquals(1, cliente.getSolicitudesAbiertas());
    }

    @Test
    void no_debe_permitir_finalizar_solicitud_si_el_numero_ya_es_cero() {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", TipoCliente.STANDARD, true, 0);
        assertThrows(IllegalStateException.class, cliente::finalizarSolicitud);
    }
    
    @Test
    void debe_obtener_correctamente_los_atributos_del_cliente() {
        Cliente cliente = new Cliente(10L, "Roberto", "roberto@example.com", TipoCliente.PREMIUM, true, 0);
        assertEquals(10L, cliente.getId());
        assertEquals("Roberto", cliente.getNombre());
        assertEquals("roberto@example.com", cliente.getEmail());
        assertEquals(TipoCliente.PREMIUM, cliente.getTipoCliente());
    }
}