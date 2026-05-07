package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TecnicoTest {

    @Test
    void no_debe_permitir_desactivar_tecnico_si_tiene_carga_trabajo() {
        Tecnico tecnico = new Tecnico(1L, "Carlos", "Sistemas", true, 1);
        assertThrows(IllegalStateException.class, tecnico::desactivar);
    }

    @Test
    void debe_permitir_desactivar_tecnico_si_no_tiene_carga() {
        Tecnico tecnico = new Tecnico(1L, "Carlos", "Sistemas", true, 0);
        tecnico.desactivar();
        assertFalse(tecnico.isActivo());
    }

    @Test
    void no_debe_permitir_incrementar_carga_si_llega_al_limite() {
        Tecnico tecnicoSaturado = new Tecnico(1L, "Luis", "Redes", true, 5);
        assertThrows(IllegalStateException.class, tecnicoSaturado::incrementarCarga);
    }

    @Test
    void no_debe_permitir_incrementar_carga_si_esta_inactivo() {
        Tecnico tecnicoInactivo = new Tecnico(1L, "Luis", "Redes", false, 0);
        assertThrows(IllegalStateException.class, tecnicoInactivo::incrementarCarga);
    }

    @Test
    void debe_reducir_carga_al_finalizar_tarea() {
        Tecnico tecnico = new Tecnico(1L, "Luis", "Redes", true, 2);
        tecnico.finalizarTarea();
        assertEquals(1, tecnico.getCargaTrabajo());
    }

    @Test
    void no_debe_permitir_finalizar_tarea_si_la_carga_ya_es_cero() {
        Tecnico tecnicoSinTareas = new Tecnico(1L, "Luis", "Redes", true, 0);
        assertThrows(IllegalStateException.class, tecnicoSinTareas::finalizarTarea);
    }
    
    @Test
    void debe_obtener_atributos_especiales_del_tecnico() {
        Tecnico tecnico = new Tecnico(1L, "Luis", "Sistemas", true, 0);
        assertEquals("Luis", tecnico.getNombre());
        assertEquals("Sistemas", tecnico.getEspecialidad());
    }
}