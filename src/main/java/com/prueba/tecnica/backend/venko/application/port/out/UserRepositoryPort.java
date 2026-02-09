package com.prueba.tecnica.backend.venko.application.port.out;

import com.prueba.tecnica.backend.venko.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
    
    void delete(User user);
}