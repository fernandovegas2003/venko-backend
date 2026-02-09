package com.prueba.tecnica.backend.venko.infrastructure.controller;

import com.prueba.tecnica.backend.venko.application.dto.AuthRequest;
import com.prueba.tecnica.backend.venko.application.dto.AuthResponse;
import com.prueba.tecnica.backend.venko.application.port.in.AuthUseCase;
import com.prueba.tecnica.backend.venko.infrastructure.controller.dto.AuthApiRequest;
import com.prueba.tecnica.backend.venko.infrastructure.controller.dto.AuthApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthUseCase authUseCase;
    
    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthApiResponse> login(@Valid @RequestBody AuthApiRequest apiRequest) {
        AuthRequest request = toAuthRequest(apiRequest);
        AuthResponse response = authUseCase.login(request);
        return ResponseEntity.ok(toAuthApiResponse(response));
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthApiResponse> register(@Valid @RequestBody AuthApiRequest apiRequest) {
        AuthRequest request = toAuthRequest(apiRequest);
        AuthResponse response = authUseCase.register(request);
        return new ResponseEntity<>(toAuthApiResponse(response), HttpStatus.CREATED);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = extractToken(token);
        authUseCase.logout(jwtToken);
        return ResponseEntity.ok().build();
    }
    
    private AuthRequest toAuthRequest(AuthApiRequest apiRequest) {
        return new AuthRequest(
            apiRequest.getUsername(),
            apiRequest.getEmail(),
            apiRequest.getPassword()
        );
    }
    
    private AuthApiResponse toAuthApiResponse(AuthResponse response) {
        AuthApiResponse apiResponse = new AuthApiResponse();
        apiResponse.setToken(response.getToken());
        apiResponse.setType(response.getType());
        apiResponse.setUserId(response.getUserId());
        apiResponse.setUsername(response.getUsername());
        apiResponse.setEmail(response.getEmail());
        apiResponse.setCreatedAt(response.getCreatedAt());
        return apiResponse;
    }
    
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
}