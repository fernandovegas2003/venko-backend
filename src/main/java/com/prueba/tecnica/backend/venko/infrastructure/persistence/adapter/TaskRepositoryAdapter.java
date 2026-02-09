package com.prueba.tecnica.backend.venko.infrastructure.persistence.adapter;

import com.prueba.tecnica.backend.venko.application.port.out.TaskRepositoryPort;
import com.prueba.tecnica.backend.venko.domain.model.Task;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.entity.TaskEntity;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.mapper.TaskPersistenceMapper;
import com.prueba.tecnica.backend.venko.infrastructure.persistence.repository.TaskJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {
    
    private final TaskJpaRepository taskJpaRepository;
    private final TaskPersistenceMapper mapper;
    
    public TaskRepositoryAdapter(TaskJpaRepository taskJpaRepository,
                                TaskPersistenceMapper mapper) {
        this.taskJpaRepository = taskJpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Task save(Task task) {
        TaskEntity entity = mapper.toEntity(task);
        TaskEntity savedEntity = taskJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Task> findAllByUserId(Long userId) {
        return taskJpaRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByUserIdAndStatus(Long userId, String status) {
        return taskJpaRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByUserIdAndPriority(Long userId, String priority) {
        return taskJpaRepository.findByUserIdAndPriority(userId, priority)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findOverdueTasksByUserId(Long userId) {
        return taskJpaRepository.findOverdueTasksByUserId(userId, LocalDateTime.now())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Task task) {
        TaskEntity entity = mapper.toEntity(task);
        taskJpaRepository.delete(entity);
    }
    
    @Override
    public boolean existsById(Long id) {
        return taskJpaRepository.existsById(id);
    }
}