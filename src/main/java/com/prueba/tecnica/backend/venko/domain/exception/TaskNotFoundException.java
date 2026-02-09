package com.prueba.tecnica.backend.venko.domain.exception;

public class TaskNotFoundException extends DomainException {
    
    public TaskNotFoundException(Long id) {
        super("Tarea no encontrada con ID: " + id);
    }
    
    public TaskNotFoundException(String message) {
        super(message);
    }
    
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}