package com.mgcss.api.controllers;

import java.util.List;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.services.SolicitudService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class SolicitudController {

    private final SolicitudService solicitudService;  

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

     
    public SolicitudResponseDTO crear(@RequestBody SolicitudRequestDTO request) {
         
         
         
         
        return new SolicitudResponseDTO(1L, request.getDescripcion(), "ABIERTA"); 
    }

     
    public List<SolicitudResponseDTO> listar() {
         
        return null; 
    }
}

