package com.prueba.tecnica.backend.venko.application.port.in;

import com.prueba.tecnica.backend.venko.application.dto.TaskRequest;
import com.prueba.tecnica.backend.venko.application.dto.TaskResponse;

import java.util.List;

public interface TaskUseCase {
    
    // CRUD Operations
    TaskResponse createTask(TaskRequest request, Long userId);
    
    TaskResponse getTaskById(Long id, Long userId);
    
    List<TaskResponse> getAllTasks(Long userId);
    
    TaskResponse updateTask(Long id, TaskRequest request, Long userId);
    
    void deleteTask(Long id, Long userId);
    
    // Business Operations
    TaskResponse startTask(Long id, Long userId);
    
    TaskResponse completeTask(Long id, Long userId);
    
    TaskResponse cancelTask(Long id, Long userId);
    
    TaskResponse reopenTask(Long id, Long userId);
    
    // Query Operations
    List<TaskResponse> getTasksByStatus(String status, Long userId);
    
    List<TaskResponse> getTasksByPriority(String priority, Long userId);
    
    List<TaskResponse> getOverdueTasks(Long userId);
}