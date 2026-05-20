package com.mgcss.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de asesoramiento global para la gestión centralizada de excepciones.
 * Intercepta los fallos de la aplicación y los transforma en respuestas HTTP semánticas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestiona excepciones de tipo IllegalArgumentException.
     * Orienta el error hacia un estado 404 cuando un recurso solicitado no existe.
     *
     * @param ex Excepción de argumento ilegal capturada.
     * @return ResponseEntity con estado 404 (Not Found) y el mensaje de error.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Gestiona excepciones de tipo IllegalStateException.
     * Transforma las violaciones de las reglas de negocio del dominio en un error de cliente.
     *
     * @param ex Excepción de estado ilegal capturada.
     * @return ResponseEntity con estado 400 (Bad Request) y la causa de la denegación.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> manejarIllegalState(IllegalStateException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Gestiona las excepciones de validación de los Data Transfer Objects (DTOs).
     * Se activa automáticamente ante restricciones fallidas en los parámetros de entrada.
     *
     * @param ex Excepción de validación de argumentos capturada.
     * @return ResponseEntity con estado 400 (Bad Request) y el mapa de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacionCampos(MethodArgumentNotValidException ex) {
        Map<String, String> erroresValidacion = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            erroresValidacion.put(error.getField(), error.getDefaultMessage())
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroresValidacion);
    }
}