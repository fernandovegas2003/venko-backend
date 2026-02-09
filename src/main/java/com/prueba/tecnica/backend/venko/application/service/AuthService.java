package com.prueba.tecnica.backend.venko.application.service;

import com.prueba.tecnica.backend.venko.application.dto.AuthRequest;
import com.prueba.tecnica.backend.venko.application.dto.AuthResponse;
import com.prueba.tecnica.backend.venko.application.port.in.AuthUseCase;
import com.prueba.tecnica.backend.venko.application.port.out.UserRepositoryPort;
import com.prueba.tecnica.backend.venko.domain.exception.AuthenticationException;
import com.prueba.tecnica.backend.venko.domain.exception.UserNotFoundException;
import com.prueba.tecnica.backend.venko.domain.model.User;
import com.prueba.tecnica.backend.venko.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService implements AuthUseCase {
    
    private final UserRepositoryPort userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    
    public AuthService(UserRepositoryPort userRepository, 
                      JwtService jwtService,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public AuthResponse login(AuthRequest request) {
        // Validar que el usuario existe
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Credenciales inválidas"));
        
        // Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Credenciales inválidas");
        }
        
        // Generar token JWT
        String token = jwtService.generateToken(user.getEmail(), user.getId());
        
        // Retornar respuesta
        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
    
    @Override
    public AuthResponse register(AuthRequest request) {
        // Verificar que el email no exista
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Verificar que el username no exista
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        // Crear usuario
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        
        // Guardar usuario
        User savedUser = userRepository.save(user);
        
        // Generar token
        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getId());
        
        // Retornar respuesta
        return new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getCreatedAt()
        );
    }
    
    @Override
    public void logout(String token) {
        
        //  invalidar el token
    }
}