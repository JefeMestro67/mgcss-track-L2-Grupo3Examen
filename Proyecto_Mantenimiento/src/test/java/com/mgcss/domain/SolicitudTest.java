package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SolicitudTest {

    // --- REGLA 1: CERRAR SOLICITUD ---
    
    @Test
    void no_debe_permitir_cerrar_solicitud_si_no_esta_en_proceso() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);
        
        assertThrows(IllegalStateException.class, solicitud::cerrar);
    }
    
    @Test
    void debe_poder_cerrar_solicitud_si_esta_en_proceso() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.EN_PROCESO);
        
        solicitud.cerrar();
        assertEquals(Estado.CERRADA, solicitud.getEstado()); // Camino feliz
    }

    // --- REGLA 2: TÉCNICO ACTIVO ---
    
    @Test
    void no_debe_permitir_asignar_tecnico_inactivo() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);
        
        Tecnico tecnicoInactivo = new Tecnico();
        tecnicoInactivo.setActivo(false);
        
        assertThrows(IllegalStateException.class, () -> {
            solicitud.asignarTecnico(tecnicoInactivo);
        });
    }

    @Test
    void debe_permitir_asignar_tecnico_activo() {
        // 1. ARRANGE
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);
        
        Tecnico tecnicoActivo = new Tecnico();
        tecnicoActivo.setActivo(true);
        
        // 2. ACT
        solicitud.asignarTecnico(tecnicoActivo); 
        
        // 3. ASSERT (Comprobamos la regla de negocio real)
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
    }

    // --- REGLA 3: NO TOCAR SOLICITUDES CERRADAS ---
    
    @Test
    void no_debe_permitir_asignar_tecnico_a_solicitud_cerrada() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.CERRADA);
        
        Tecnico tecnico = new Tecnico();
        tecnico.setActivo(true);
        
        assertThrows(IllegalStateException.class, () -> {
            solicitud.asignarTecnico(tecnico);
        });
    }
}