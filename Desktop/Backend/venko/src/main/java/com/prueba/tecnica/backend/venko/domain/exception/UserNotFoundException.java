package com.prueba.tecnica.backend.venko.domain.exception;

public class UserNotFoundException extends DomainException {
    
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }
    
    public UserNotFoundException(String email) {
        super("Usuario no encontrado con email: " + email);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}