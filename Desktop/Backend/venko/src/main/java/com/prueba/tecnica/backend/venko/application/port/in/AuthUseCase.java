package com.prueba.tecnica.backend.venko.application.port.in;

import com.prueba.tecnica.backend.venko.application.dto.AuthRequest;
import com.prueba.tecnica.backend.venko.application.dto.AuthResponse;

public interface AuthUseCase {
    
    AuthResponse login(AuthRequest request);
    
    AuthResponse register(AuthRequest request);
    
    void logout(String token);
}