package com.mgcss.services;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.Estado;
import com.mgcss.infrastructure.ClienteRepository;
import com.mgcss.infrastructure.SolicitudRepository;
import com.mgcss.infrastructure.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ClienteRepository clienteRepository;

    // Inyectamos las tres dependencias para poder orquestar todo el sistema
    public SolicitudService(SolicitudRepository solicitudRepository, 
                            TecnicoRepository tecnicoRepository,
                            ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public Solicitud crearSolicitud(Long clienteId, String descripcion) {
        // 1. Buscamos el cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));
                
        // 2. El dominio verifica si puede crearla y aumenta su contador de solicitudes
        cliente.crearSolicitud(); 

        // 3. Creamos la solicitud real
        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setCliente(cliente);
        nuevaSolicitud.setDescripcion(descripcion);
        nuevaSolicitud.setEstado(Estado.ABIERTA);
        nuevaSolicitud.setFechaCreacion(LocalDateTime.now());
        
        // 4. Guardamos ambos estados para mantener la consistencia
        clienteRepository.save(cliente);
        return solicitudRepository.save(nuevaSolicitud);
    }

    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
                
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));

        // Lógica de Dominio Conjunta
        solicitud.asignarTecnico(tecnico); 
        tecnico.incrementarCarga(); // Actualizamos la carga del técnico

        // Guardamos los cambios
        tecnicoRepository.save(tecnico);
        solicitudRepository.save(solicitud);
    }
    
    public void cerrarSolicitud(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));

        // 1. Cerramos la solicitud
        solicitud.cerrar();

        // 2. Liberamos al cliente (baja su contador)
        Cliente cliente = solicitud.getCliente();
        cliente.finalizarSolicitud();
        clienteRepository.save(cliente);

        // 3. Liberamos al técnico (si lo hay)
        Tecnico tecnico = solicitud.getTecnicoAsignado();
        if (tecnico != null) {
            tecnico.finalizarTarea();
            tecnicoRepository.save(tecnico);
        }

        solicitudRepository.save(solicitud);
    }
}