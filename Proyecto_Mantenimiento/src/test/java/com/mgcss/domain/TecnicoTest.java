package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TecnicoTest {

    // --- REGLA 1: DESACTIVAR TÉCNICO ---

    @Test
    void no_debe_permitir_desactivar_tecnico_si_tiene_carga_trabajo() {
        // Creamos un técnico activo con 1 tarea pendiente
        Tecnico tecnico = new Tecnico(1L, "Carlos", true, 1);
        
        // Debe lanzar excepción al intentar desactivarlo
        assertThrows(IllegalStateException.class, tecnico::desactivar);
    }

    @Test
    void debe_permitir_desactivar_tecnico_si_no_tiene_carga() {
        // Creamos un técnico activo sin tareas
        Tecnico tecnico = new Tecnico(1L, "Carlos", true, 0);
        
        tecnico.desactivar();
        
        // Verificamos que el estado cambió a falso
        assertFalse(tecnico.isActivo());
    }

    // --- REGLA 2: LÍMITE DE CARGA ---

    @Test
    void no_debe_permitir_incrementar_carga_si_llega_al_limite() {
        // Creamos un técnico con el máximo de 5 tareas permitido
        Tecnico tecnicoSaturado = new Tecnico(1L, "Carlos", true, 5);
        
        // Debe lanzar excepción al intentar asignar la sexta
        assertThrows(IllegalStateException.class, tecnicoSaturado::incrementarCarga);
    }

    @Test
    void no_debe_permitir_incrementar_carga_si_esta_inactivo() {
        // Creamos un técnico inactivo
        Tecnico tecnicoInactivo = new Tecnico(1L, "Carlos", false, 0);
        
        assertThrows(IllegalStateException.class, tecnicoInactivo::incrementarCarga);
    }

    // --- REGLA 3: FINALIZAR TAREAS ---

    @Test
    void debe_reducir_carga_al_finalizar_tarea() {
        // Técnico con 2 tareas
        Tecnico tecnico = new Tecnico(1L, "Carlos", true, 2);
        
        tecnico.finalizarTarea();
        
        // Comprobamos que bajó a 1
        assertEquals(1, tecnico.getCargaTrabajo());
    }

    @Test
    void no_debe_permitir_finalizar_tarea_si_la_carga_ya_es_cero() {
        Tecnico tecnicoSinTareas = new Tecnico(1L, "Carlos", true, 0);
        
        // No se puede bajar de cero
        assertThrows(IllegalStateException.class, tecnicoSinTareas::finalizarTarea);
    }
}
