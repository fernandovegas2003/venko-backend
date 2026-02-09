package com.prueba.tecnica.backend.venko.domain.exception;

public class AuthenticationException extends DomainException {
    
    public AuthenticationException() {
        super("Credenciales de autenticación inválidas");
    }
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}