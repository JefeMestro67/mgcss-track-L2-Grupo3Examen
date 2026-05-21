package com.mgcss.infrastructure;

import java.util.Optional;
import java.util.List; 

import com.mgcss.domain.Solicitud;

public interface SolicitudRepository {
    Solicitud save(Solicitud solicitud);
    Optional<Solicitud> findById(Long id);
    List<Solicitud> findAll(); 
}