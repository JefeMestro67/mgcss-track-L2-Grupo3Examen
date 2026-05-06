package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;

class SolicitudTest {

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
        assertEquals(Estado.CERRADA, solicitud.getEstado());
    }

    @Test
    void no_debe_permitir_asignar_tecnico_inactivo() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);
        
        Tecnico tecnicoInactivo = new Tecnico();
        tecnicoInactivo.setActivo(false);
        
        assertThrows(IllegalStateException.class, () -> solicitud.asignarTecnico(tecnicoInactivo));
    }

    @Test
    void debe_permitir_asignar_tecnico_activo() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.ABIERTA);
        
        Tecnico tecnicoActivo = new Tecnico();
        tecnicoActivo.setActivo(true);
        
        solicitud.asignarTecnico(tecnicoActivo); 
        
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
    }

    @Test
    void no_debe_permitir_asignar_tecnico_a_solicitud_cerrada() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(Estado.CERRADA);
        
        Tecnico tecnico = new Tecnico();
        tecnico.setActivo(true);
        
        assertThrows(IllegalStateException.class, () -> solicitud.asignarTecnico(tecnico));
    }
    
    @Test
    void debe_probar_getters_y_setters_basicos() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(15L);
        solicitud.setDescripcion("Prueba de descripción");
        
        LocalDateTime now = LocalDateTime.now();
        solicitud.setFechaCreacion(now);
        solicitud.setFechaCierre(now);
        
        assertEquals(15L, solicitud.getId());
        assertEquals("Prueba de descripción", solicitud.getDescripcion());
        assertEquals(now, solicitud.getFechaCreacion());
        assertEquals(now, solicitud.getFechaCierre());
    }
    @Test
    void debe_guardar_el_tecnico_al_asignarlo() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(Estado.ABIERTA);
        Tecnico tecnico = new Tecnico(true);

        solicitud.asignarTecnico(tecnico);

        assertEquals(tecnico, solicitud.getTecnicoAsignado()); // <-- Esto cubre el getter
    }
}