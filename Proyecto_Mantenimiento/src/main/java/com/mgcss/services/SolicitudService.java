package com.mgcss.services;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.Estado;
import com.mgcss.infrastructure.ClienteRepository;
import com.mgcss.infrastructure.SolicitudRepository;
import com.mgcss.infrastructure.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; //  Importación crucial

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, 
                            TecnicoRepository tecnicoRepository,
                            ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional // Asegura que el cliente y la solicitud se guardan juntos o nada
    public Solicitud crearSolicitud(Long clienteId, String descripcion) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));
                
        cliente.crearSolicitud(); 

        Solicitud nuevaSolicitud = new Solicitud(
            null, 
            cliente, 
            descripcion, 
            LocalDateTime.now(), 
            Estado.ABIERTA, 
            null, 
            null
        );
        
        clienteRepository.save(cliente);
        return solicitudRepository.save(nuevaSolicitud);
    }

    @Transactional // Evita desajustes si falla el cambio de estado de la solicitud
    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
                
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));

        solicitud.asignarTecnico(tecnico); 
        tecnico.incrementarCarga(); 

        tecnicoRepository.save(tecnico);
        solicitudRepository.save(solicitud);
    }
    
    @Transactional // Atomicidad pura para liberar cliente, técnico e incidencia a la vez
    public void cerrarSolicitud(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));

        solicitud.cerrar();

        Cliente cliente = solicitud.getCliente();
        cliente.finalizarSolicitud();
        clienteRepository.save(cliente);

        Tecnico tecnico = solicitud.getTecnicoAsignado();
        if (tecnico != null) {
            tecnico.finalizarTarea();
            tecnicoRepository.save(tecnico);
        }

        solicitudRepository.save(solicitud);
    }

    @Transactional // Control transaccional y validación de seguridad
    public void reabrirSolicitud(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));

        // Al verificar el técnico asignado antes de mutar nada
        Tecnico tecnico = solicitud.getTecnicoAsignado();
        if (tecnico != null && !tecnico.isActivo()) {
            throw new IllegalStateException("No se puede reabrir la solicitud porque el técnico asignado está inactivo");
        }

        solicitud.reabrir();

        // Al reabrir, el cliente vuelve a tener una solicitud activa
        Cliente cliente = solicitud.getCliente();
        if (cliente != null) {
            cliente.crearSolicitud(); 
            clienteRepository.save(cliente);
        }

        // El técnico vuelve a tener carga de trabajo de forma segura
        if (tecnico != null) {
            tecnico.incrementarCarga();
            tecnicoRepository.save(tecnico);
        }

        solicitudRepository.save(solicitud);
    }

    public Solicitud buscarPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
    }

    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAll();
    }
}