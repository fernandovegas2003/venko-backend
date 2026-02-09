package com.prueba.tecnica.backend.venko.infrastructure.persistence.adapter;

import com.prueba.tecnica.backend.venko.application.port.out.UserRepositoryPort;
import com.prueba.tecnica.backend.venko.domain.model.User;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.entity.UserEntity;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;
    
    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, 
                                UserPersistenceMapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
    
    @Override
    public void delete(User user) {
        UserEntity entity = mapper.toEntity(user);
        userJpaRepository.delete(entity);
    }
}