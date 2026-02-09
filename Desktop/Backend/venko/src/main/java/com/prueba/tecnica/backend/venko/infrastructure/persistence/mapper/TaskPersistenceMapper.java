package com.prueba.tecnica.backend.venko.infrastructure.persistence.mapper;

import com.prueba.tecnica.backend.venko.domain.model.Task;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskPersistenceMapper {
    
    public TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setStatus(task.getStatus());
        entity.setPriority(task.getPriority());
        entity.setDueDate(task.getDueDate());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setUpdatedAt(task.getUpdatedAt());
        entity.setUserId(task.getUserId());
        
        return entity;
    }
    
    public Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Task(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getStatus(),
            entity.getPriority(),
            entity.getDueDate(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getUserId()
        );
    }
}