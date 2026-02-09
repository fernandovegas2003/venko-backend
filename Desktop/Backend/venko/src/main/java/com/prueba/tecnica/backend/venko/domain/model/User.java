package com.prueba.tecnica.backend.venko.domain.model;

import com.prueba.tecnica.backend.venko.domain.model.interfaces.IUser;
import java.time.LocalDateTime;

public class User implements IUser {

    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    // Constructor para registro
    public User(String username, String email, String password) {
        String usernameTrim = validateUsername(username);
        String emailTrim = validateEmail(email);
        validatePassword(password);

        this.username = usernameTrim;
        this.email = emailTrim;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor para cargar desde BD
    public User(Long id, String username, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    // Constructor vacío para JPA
    protected User() {}

    // ========== VALIDACIONES ==========
    private String validateUsername(String username) {
        String value = username == null ? "" : username.trim();

        if (value.isEmpty() || value.length() < 3 || value.length() > 20) {
            if (value.isEmpty()) {
                throw new RuntimeException("El nombre de usuario es obligatorio");
            }
            if (value.length() < 3) {
                throw new RuntimeException("El usuario debe tener al menos 3 caracteres");
            }
            throw new RuntimeException("El usuario no puede tener más de 20 caracteres");
        }
        return value;
    }

    private String validateEmail(String email) {
        String value = email == null ? "" : email.trim().toLowerCase();

        int atIndex = value.indexOf('@');
        int dotIndex = value.lastIndexOf('.');

        boolean valid = !value.isEmpty()
                && atIndex > 0
                && atIndex == value.lastIndexOf('@')
                && dotIndex > atIndex + 1
                && dotIndex < value.length() - 1;

        if (!valid) {
            if (value.isEmpty()) {
                throw new RuntimeException("El email es obligatorio");
            }
            if (!value.contains("@")) {
                throw new RuntimeException("El email debe contener @");
            }
            if (!value.contains(".")) {
                throw new RuntimeException("El email debe contener un punto");
            }
            throw new RuntimeException("Formato de email inválido");
        }
        return value;
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()
                || password.length() < 8
                || password.length() > 200) {

            if (password == null || password.trim().isEmpty()) {
                throw new RuntimeException("La contraseña es obligatoria");
            }
            if (password.length() < 8) {
                throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
            }
            throw new RuntimeException("La contraseña no puede tener más de 50 caracteres");
        }
    }

    // ========== IMPLEMENTACIÓN DE IUser ==========
    @Override
    public Long getId() { 
        return id; 
    }

    @Override
    public String getUsername() { 
        return username; 
    }

    @Override
    public String getEmail() { 
        return email; 
    }

    @Override
    public String getPassword() { 
        return password; 
    }

    @Override
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    @Override
    public boolean verifyPassword(String passwordToCheck) {
        return passwordToCheck != null && this.password.equals(passwordToCheck);
    }

    @Override
    public void changePassword(String newPassword) {
        validatePassword(newPassword);
        this.password = newPassword;
    }

    @Override
    public boolean isValidEmail() {
        if (email == null) return false;
        
        int atIndex = email.indexOf('@');
        int dotIndex = email.lastIndexOf('.');
        
        return !email.isEmpty()
                && atIndex > 0
                && atIndex == email.lastIndexOf('@')
                && dotIndex > atIndex + 1
                && dotIndex < email.length() - 1;
    }

    @Override
    public boolean isPasswordStrong() {
        if (password == null) return false;
        return password.length() >= 8 && password.length() <= 50;
    }

    @Override
    public boolean canLogin() {
        return isValidEmail() && isPasswordStrong();
    }

    @Override
    public boolean isAccountActive() {
        
        return true;
    }

    // ========== SETTERS ==========
    public void setId(Long id) { 
        this.id = id; 
    }

    public void setUsername(String username) {
        this.username = validateUsername(username);
    }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User other)) return false;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        return email != null && email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : (email != null ? email.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}