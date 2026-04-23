package com.mgcss.services;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.SolicitudRepository;
import com.mgcss.infrastructure.TecnicoRepository;

public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;

    // Inyección por constructor (Obligatorio para poder testear en aislamiento)
    public SolicitudService(SolicitudRepository solicitudRepository, TecnicoRepository tecnicoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        
        // 1. Protegemos la búsqueda: si el Optional está vacío, lanzamos nuestra excepción
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
                
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));

        // 2. El servicio llama al dominio.
        solicitud.asignarTecnico(tecnico);

        // 3. Guardamos el estado final
        solicitudRepository.save(solicitud);
    }
    
    public void cerrarSolicitud(Long solicitudId) {
        // 1. Recuperamos la solicitud
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));

        // 2. El servicio llama al dominio
        solicitud.cerrar();

        // 3. Guardamos el cambio
        solicitudRepository.save(solicitud);
    }
    
    public Solicitud crearSolicitud() {
        // Creamos una solicitud pura de dominio, sin ID (porque lo genera la BD) y en estado inicial ABIERTA
        Solicitud nuevaSolicitud = new Solicitud(null, com.mgcss.domain.Estado.ABIERTA, java.time.LocalDateTime.now());
        
        // Delegamos en el repositorio (el puerto) para que la guarde
        return solicitudRepository.save(nuevaSolicitud);
    }
}