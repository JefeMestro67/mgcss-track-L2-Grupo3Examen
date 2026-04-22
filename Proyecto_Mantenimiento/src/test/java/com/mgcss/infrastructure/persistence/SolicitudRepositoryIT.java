package com.mgcss.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mgcss.domain.Estado;

@DataJpaTest
@Tag("integration")
class SolicitudRepositoryIT {

    @Autowired
    private JpaSolicitudRepository repository;

    @Test
    void debe_guardar_y_recuperar_una_solicitud_real() {
        // 1. ARRANGE: Creamos la entidad (la caja tonta de infra)
        SolicitudEntity entity = new SolicitudEntity(null, Estado.ABIERTA, LocalDateTime.now());

        // 2. ACT: La guardamos de verdad en H2
        SolicitudEntity guardada = repository.save(entity);

        // 3. ASSERT: La buscamos por ID y comprobamos que no se ha perdido nada
        Optional<SolicitudEntity> recuperada = repository.findById(guardada.getId());
        
        assertTrue(recuperada.isPresent(), "La solicitud debería estar en la base de datos");
        assertEquals(Estado.ABIERTA, recuperada.get().getEstado());
        System.out.println("ID generado por H2: " + recuperada.get().getId());
    }
}
