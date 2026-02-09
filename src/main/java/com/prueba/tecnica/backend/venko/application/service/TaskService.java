package com.prueba.tecnica.backend.venko.application.service;

import com.prueba.tecnica.backend.venko.application.dto.TaskRequest;
import com.prueba.tecnica.backend.venko.application.dto.TaskResponse;
import com.prueba.tecnica.backend.venko.application.port.in.TaskUseCase;
import com.prueba.tecnica.backend.venko.application.port.out.TaskRepositoryPort;
import com.prueba.tecnica.backend.venko.application.port.out.UserRepositoryPort;
import com.prueba.tecnica.backend.venko.domain.exception.TaskNotFoundException;
import com.prueba.tecnica.backend.venko.domain.exception.UserNotFoundException;
import com.prueba.tecnica.backend.venko.domain.model.Task;
import com.prueba.tecnica.backend.venko.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService implements TaskUseCase {
    
    private final TaskRepositoryPort taskRepository;
    private final UserRepositoryPort userRepository;
    
    public TaskService(TaskRepositoryPort taskRepository,
                      UserRepositoryPort userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public TaskResponse createTask(TaskRequest request, Long userId) {
        // Verificar que el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Crear tarea
        Task task = new Task(
            request.getTitle(),
            request.getDescription(),
            request.getPriority(),
            request.getDueDate(),
            userId
        );
        
        // Guardar tarea
        Task savedTask = taskRepository.save(task);
        
        // Convertir a DTO
        return toResponse(savedTask);
    }
    
    @Override
    public TaskResponse getTaskById(Long id, Long userId) {
        // Buscar tarea
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        // Verificar que pertenece al usuario
        if (!task.belongsToUser(userId)) {
            throw new IllegalArgumentException("No tienes permisos para ver esta tarea");
        }
        
        return toResponse(task);
    }
    
    @Override
    public List<TaskResponse> getAllTasks(Long userId) {
        // Obtener todas las tareas del usuario
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        
        // Convertir a DTOs
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public TaskResponse updateTask(Long id, TaskRequest request, Long userId) {
        // Buscar tarea
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        // Verificar que pertenece al usuario
        if (!task.belongsToUser(userId)) {
            throw new IllegalArgumentException("No tienes permisos para actualizar esta tarea");
        }
        
        // Actualizar tarea
        task.update(
            request.getTitle(),
            request.getDescription(),
            request.getPriority(),
            request.getDueDate()
        );
        
        // Guardar cambios
        Task updatedTask = taskRepository.save(task);
        
        return toResponse(updatedTask);
    }
    
    @Override
    public void deleteTask(Long id, Long userId) {
        // Buscar tarea
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        // Verificar que pertenece al usuario
        if (!task.belongsToUser(userId)) {
            throw new IllegalArgumentException("No tienes permisos para eliminar esta tarea");
        }
        
        // Verificar que se puede eliminar
        if (!task.canBeDeleted()) {
            throw new IllegalStateException("No se puede eliminar una tarea en progreso");
        }
        
        // Eliminar tarea
        taskRepository.delete(task);
    }
    
    @Override
    public TaskResponse startTask(Long id, Long userId) {
        Task task = getTaskAndValidateOwnership(id, userId);
        task.start();
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    @Override
    public TaskResponse completeTask(Long id, Long userId) {
        Task task = getTaskAndValidateOwnership(id, userId);
        task.complete();
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    @Override
    public TaskResponse cancelTask(Long id, Long userId) {
        Task task = getTaskAndValidateOwnership(id, userId);
        task.cancel();
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    @Override
    public TaskResponse reopenTask(Long id, Long userId) {
        Task task = getTaskAndValidateOwnership(id, userId);
        task.reopen();
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    @Override
    public List<TaskResponse> getTasksByStatus(String status, Long userId) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(userId, status);
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TaskResponse> getTasksByPriority(String priority, Long userId) {
        List<Task> tasks = taskRepository.findByUserIdAndPriority(userId, priority);
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TaskResponse> getOverdueTasks(Long userId) {
        List<Task> tasks = taskRepository.findOverdueTasksByUserId(userId);
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    // Método auxiliar
    private Task getTaskAndValidateOwnership(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        if (!task.belongsToUser(userId)) {
            throw new IllegalArgumentException("No tienes permisos sobre esta tarea");
        }
        
        return task;
    }
    
    // Método de conversión
    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getDueDate(),
            task.getCreatedAt(),
            task.getUpdatedAt(),
            task.getUserId(),
            task.isOverdue()
        );
    }
}