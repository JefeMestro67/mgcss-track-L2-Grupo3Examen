package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.TecnicoRepositoryAdapter;

@DataJpaTest
@Tag("integration")
class TecnicoRepositoryTest {

    @Autowired
    private JpaTecnicoRepository jpaRepository;

    @Test
    void debe_guardar_y_recuperar_un_tecnico_en_h2() {
        TecnicoEntity entity = new TecnicoEntity();
        entity.setId(null);
        entity.setNombre("Carlos");
        entity.setEspecialidad("Redes");
        entity.setActivo(true);
        entity.setCargaTrabajo(0);

        TecnicoEntity guardado = jpaRepository.save(entity);

        Optional<TecnicoEntity> recuperado = jpaRepository.findById(guardado.getId());
        
        assertTrue(recuperado.isPresent());
        assertEquals("Carlos", recuperado.get().getNombre());
        assertEquals(0, recuperado.get().getCargaTrabajo());
    }

    @Test
    void debe_funcionar_el_ciclo_completo_con_el_adaptador_de_tecnico() {
        TecnicoRepositoryAdapter adapter = new TecnicoRepositoryAdapter(jpaRepository);
        
        Tecnico tecnicoDominio = new Tecnico(null, "Ana", "Sistemas", true, 3);

        Tecnico guardado = adapter.save(tecnicoDominio);
        Optional<Tecnico> recuperado = adapter.findById(guardado.getId());

        assertTrue(recuperado.isPresent());
        assertEquals("Ana", recuperado.get().getNombre());
        assertEquals(3, recuperado.get().getCargaTrabajo());
        assertTrue(recuperado.get().isActivo());
    }

    @Test
    void debe_retornar_vacio_si_el_tecnico_no_existe() {
        TecnicoRepositoryAdapter adapter = new TecnicoRepositoryAdapter(jpaRepository);
        Optional<Tecnico> recuperado = adapter.findById(999L);
        assertTrue(recuperado.isEmpty());
    }

    @Test
    void debe_actualizar_un_tecnico() {
        TecnicoRepositoryAdapter adapter = new TecnicoRepositoryAdapter(jpaRepository);
        
        Tecnico tecnico = new Tecnico(null, "Técnico Inicial", "Sistemas", true, 0);
        Tecnico guardado = adapter.save(tecnico);
        
        // Simular actualización con nuevo constructor
        Tecnico tecnicoAActualizar = new Tecnico(guardado.getId(), "Técnico Actualizado", guardado.getEspecialidad(), guardado.isActivo(), guardado.getCargaTrabajo());
        Tecnico actualizado = adapter.save(tecnicoAActualizar);
        
        assertEquals("Técnico Actualizado", actualizado.getNombre());
    }
}