package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ClienteTest {

    // --- REGLA 1: DESACTIVAR CLIENTE ---

    @Test
    void no_debe_permitir_desactivar_cliente_si_tiene_solicitudes_abiertas() {
        // Creamos un cliente activo con 1 solicitud abierta
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(1);
        
        // Debe lanzar excepción al intentar desactivarlo
        assertThrows(IllegalStateException.class, cliente::desactivar);
    }

    @Test
    void debe_permitir_desactivar_cliente_si_no_tiene_solicitudes_abiertas() {
        // Creamos un cliente activo sin solicitudes
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(0);
        
        cliente.desactivar();
        
        // Verificamos que el estado cambió a falso
        assertFalse(cliente.isActivo());
    }

    // --- REGLA 2: LÍMITE DE SOLICITUDES ---

    @Test
    void no_debe_permitir_crear_solicitud_si_llega_al_limite() {
        // Creamos un cliente con 3 solicitudes abiertas (el máximo permitido)
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(3);
        
        // Debe lanzar excepción al intentar crear una cuarta
        assertThrows(IllegalStateException.class, cliente::crearSolicitud);
    }

    @Test
    void no_debe_permitir_crear_solicitud_si_esta_inactivo() {
        // Creamos un cliente inactivo
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(false);
        cliente.setSolicitudesAbiertas(0);
        
        assertThrows(IllegalStateException.class, cliente::crearSolicitud);
    }
    
    @Test
    void debe_permitir_crear_solicitud_si_esta_activo_y_por_debajo_del_limite() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(1);
        
        cliente.crearSolicitud();
        
        assertEquals(2, cliente.getSolicitudesAbiertas());
    }

    // --- REGLA 3: FINALIZAR SOLICITUD ---

    @Test
    void debe_reducir_solicitudes_abiertas_al_finalizar_solicitud() {
        // Cliente con 2 solicitudes
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(2);
        
        cliente.finalizarSolicitud();
        
        // Comprobamos que bajó a 1
        assertEquals(1, cliente.getSolicitudesAbiertas());
    }

    @Test
    void no_debe_permitir_finalizar_solicitud_si_el_numero_ya_es_cero() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipoCliente(TipoCliente.STANDARD);
        cliente.setActivo(true);
        cliente.setSolicitudesAbiertas(0);
        
        // No se puede bajar de cero
        assertThrows(IllegalStateException.class, cliente::finalizarSolicitud);
    }
}
