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
        // 1. ARRANGE: Creamos la entidad de infraestructura con el constructor vacío
        TecnicoEntity entity = new TecnicoEntity();
        entity.setId(null);
        entity.setNombre("Carlos");
        entity.setEspecialidad("Redes"); // Especialidad requerida por la entidad
        entity.setActivo(true);
        entity.setCargaTrabajo(0);

        // 2. ACT: Guardamos directamente con el repositorio JPA
        TecnicoEntity guardado = jpaRepository.save(entity);

        // 3. ASSERT: Comprobamos la persistencia real
        Optional<TecnicoEntity> recuperado = jpaRepository.findById(guardado.getId());
        
        assertTrue(recuperado.isPresent());
        assertEquals("Carlos", recuperado.get().getNombre());
        assertEquals(0, recuperado.get().getCargaTrabajo());
    }

    @Test
    void debe_funcionar_el_ciclo_completo_con_el_adaptador_de_tecnico() {
        // 1. ARRANGE: Instanciamos el adaptador manual
        TecnicoRepositoryAdapter adapter = new TecnicoRepositoryAdapter(jpaRepository);
        
        // Objeto de DOMINIO usando el constructor por defecto y setters
        Tecnico tecnicoDominio = new Tecnico();
        tecnicoDominio.setId(null);
        tecnicoDominio.setNombre("Ana");
        tecnicoDominio.setActivo(true);
        tecnicoDominio.setCargaTrabajo(3);

        // 2. ACT: Guardar y Recuperar a través del Adaptador
        Tecnico guardado = adapter.save(tecnicoDominio);
        Optional<Tecnico> recuperado = adapter.findById(guardado.getId());

        // 3. ASSERT: Verificamos que el mapeo Dominio -> Entity -> Dominio es correcto
        assertTrue(recuperado.isPresent());
        assertEquals("Ana", recuperado.get().getNombre());
        assertEquals(3, recuperado.get().getCargaTrabajo());
        assertTrue(recuperado.get().isActivo());
    }
}
