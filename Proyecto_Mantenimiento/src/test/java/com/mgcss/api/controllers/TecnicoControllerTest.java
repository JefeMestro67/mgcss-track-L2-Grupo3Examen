package com.mgcss.api.controllers;

import com.mgcss.services.TecnicoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TecnicoController.class)
public class TecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TecnicoService tecnicoService;

    // 1. Test de PUT → Desactivar Técnico
    @Test
    void cuandoDesactivarTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).desactivarTecnico(1L);

        mockMvc.perform(put("/api/tecnicos/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    // 2. Test de PUT → Asignar Tarea (Incrementar carga)
    @Test
    void cuandoAsignarTareaATecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).asignarNuevaTarea(1L);

        mockMvc.perform(put("/api/tecnicos/1/asignar-tarea"))
                .andExpect(status().isNoContent());
    }

    // 3. Test de PUT → Finalizar Tarea (Reducir carga)
    @Test
    void cuandoFinalizarTareaDeTecnico_entoncesDevuelveNoContent() throws Exception {
        Mockito.doNothing().when(tecnicoService).finalizarTarea(1L);

        mockMvc.perform(put("/api/tecnicos/1/finalizar-tarea"))
                .andExpect(status().isNoContent());
    }
}
