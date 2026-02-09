package com.prueba.tecnica.backend.venko.infrastructure.persistence.mapper;

import com.prueba.tecnica.backend.venko.domain.model.User;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {
    
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setCreatedAt(user.getCreatedAt());
        
        return entity;
    }
    
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getCreatedAt()
        );
    }
}