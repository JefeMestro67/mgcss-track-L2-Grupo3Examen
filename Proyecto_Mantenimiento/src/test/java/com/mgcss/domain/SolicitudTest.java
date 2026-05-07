package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.time.LocalDateTime;

class SolicitudTest {

    @Test
    void no_debe_permitir_cerrar_solicitud_si_no_esta_en_proceso() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        assertThrows(IllegalStateException.class, solicitud::cerrar);
    }
    
    @Test
    void debe_poder_cerrar_solicitud_si_esta_en_proceso() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.EN_PROCESO, null, null);
        solicitud.cerrar();
        assertEquals(Estado.CERRADA, solicitud.getEstado());
    }

    @Test
    void no_debe_permitir_asignar_tecnico_inactivo() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnicoInactivo = new Tecnico(1L, "Juan", "Redes", false, 0);
        assertThrows(IllegalStateException.class, () -> solicitud.asignarTecnico(tecnicoInactivo));
    }

    @Test
    void debe_permitir_asignar_tecnico_activo() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnicoActivo = new Tecnico(1L, "Juan", "Redes", true, 0);
        solicitud.asignarTecnico(tecnicoActivo); 
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
    }

    @Test
    void no_debe_permitir_asignar_tecnico_a_solicitud_cerrada() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.CERRADA, null, null);
        Tecnico tecnicoActivo = new Tecnico(1L, "Juan", "Redes", true, 0);
        assertThrows(IllegalStateException.class, () -> solicitud.asignarTecnico(tecnicoActivo));
    }
    
    @Test
    void debe_probar_getters_y_setters_basicos() {
        LocalDateTime now = LocalDateTime.now();
        Solicitud solicitud = new Solicitud(15L, null, "Prueba de descripción", now, Estado.ABIERTA, null, now);
        assertEquals(15L, solicitud.getId());
        assertEquals("Prueba de descripción", solicitud.getDescripcion());
        assertEquals(now, solicitud.getFechaCreacion());
        assertEquals(now, solicitud.getFechaCierre());
    }

    @Test
    void debe_guardar_el_tecnico_al_asignarlo() {
        Solicitud solicitud = new Solicitud(1L, null, "Desc", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnico = new Tecnico(1L, "Juan", "Redes", true, 0);
        solicitud.asignarTecnico(tecnico);
        assertEquals(tecnico, solicitud.getTecnicoAsignado()); 
    }
    
    @Test
    void debe_permitir_reabrir_una_solicitud_cerrada() {
        // 1. Crear solicitud y pasarla a EN_PROCESO asignando un técnico
        Solicitud solicitud = new Solicitud(1L, null, "Error de red", LocalDateTime.now(), Estado.ABIERTA, null, null);
        Tecnico tecnico = new Tecnico(1L, "Juan", "Sistemas", true, 0);
        solicitud.asignarTecnico(tecnico);
        
        // 2. Cerrar la solicitud
        solicitud.cerrar();
        assertEquals(Estado.CERRADA, solicitud.getEstado());
        
        // 3. Intentar REABRIR
        solicitud.reabrir();
        
        // 4. Verificar que el estado vuelve a ser EN_PROCESO y se limpia la fecha de cierre
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
        assertNull(solicitud.getFechaCierre());
    }
    
    @Test
    void debe_registrar_el_historial_de_cambios_de_estado() {
        // 1. Crear solicitud (Estado inicial: ABIERTA)
        Solicitud solicitud = new Solicitud(1L, null, "Test historial", LocalDateTime.now(), Estado.ABIERTA, null, null);
        
        // 2. Transición 1: Asignar técnico (ABIERTA -> EN_PROCESO)
        Tecnico tecnico = new Tecnico(1L, "Luis", "Soporte", true, 0);
        solicitud.asignarTecnico(tecnico);
        
        // 3. Transición 2: Cerrar (EN_PROCESO -> CERRADA)
        solicitud.cerrar();
        
        // 4. Transición 3: Reabrir (CERRADA -> EN_PROCESO)
        solicitud.reabrir();
        
        // 5. Verificación (Este método getHistorial aún no existe, dará error en rojo)
        assertEquals(3, solicitud.getHistorial().size());
        assertEquals(Estado.ABIERTA, solicitud.getHistorial().get(0).getEstadoAnterior());
        assertEquals(Estado.EN_PROCESO, solicitud.getHistorial().get(0).getEstadoNuevo());
    }
}