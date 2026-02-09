package com.prueba.tecnica.backend.venko.application.port.out;

import com.prueba.tecnica.backend.venko.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    
    Task save(Task task);
    
    Optional<Task> findById(Long id);
    
    List<Task> findAllByUserId(Long userId);
    
    List<Task> findByUserIdAndStatus(Long userId, String status);
    
    List<Task> findByUserIdAndPriority(Long userId, String priority);
    
    List<Task> findOverdueTasksByUserId(Long userId);
    
    void delete(Task task);
    
    boolean existsById(Long id);
}