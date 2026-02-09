package com.prueba.tecnica.backend.venko.domain.model.interfaces;


import java.time.LocalDateTime;

public interface IUser {
    
    // Métodos de acceso
    Long getId();
    String getUsername();
    String getEmail();
    String getPassword();
    LocalDateTime getCreatedAt();
    
    // Métodos de negocio
    boolean verifyPassword(String passwordToCheck);
    void changePassword(String newPassword);
    
    // Métodos de validación
    boolean isValidEmail();
    boolean isPasswordStrong();
    
    // Métodos de estado
    boolean canLogin();
    boolean isAccountActive();
}