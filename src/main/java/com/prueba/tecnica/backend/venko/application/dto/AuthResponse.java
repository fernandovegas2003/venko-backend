package com.prueba.tecnica.backend.venko.application.dto;

import java.time.LocalDateTime;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    
    // Constructores
    public AuthResponse() {}
    
    public AuthResponse(String token, Long userId, String username, String email, LocalDateTime createdAt) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }
    
    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   
    @Override
    public String toString() {
        return "AuthResponse{" +
            "token='" + (token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "null") + '\'' +
            ", type='" + type + '\'' +
            ", userId=" + userId +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}