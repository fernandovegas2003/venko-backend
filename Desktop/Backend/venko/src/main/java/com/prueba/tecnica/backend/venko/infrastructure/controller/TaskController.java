package com.prueba.tecnica.backend.venko.infrastructure.controller;

import com.prueba.tecnica.backend.venko.application.dto.TaskRequest;
import com.prueba.tecnica.backend.venko.application.dto.TaskResponse;
import com.prueba.tecnica.backend.venko.application.port.in.TaskUseCase;
import com.prueba.tecnica.backend.venko.infrastructure.controller.dto.TaskApiRequest;
import com.prueba.tecnica.backend.venko.infrastructure.controller.dto.TaskApiResponse;
import com.prueba.tecnica.backend.venko.infrastructure.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    private final TaskUseCase taskUseCase;
    private final JwtService jwtService;
    
    public TaskController(TaskUseCase taskUseCase, JwtService jwtService) {
        this.taskUseCase = taskUseCase;
        this.jwtService = jwtService;
    }
    
    @PostMapping
    public ResponseEntity<TaskApiResponse> createTask(
            @Valid @RequestBody TaskApiRequest apiRequest,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskRequest taskRequest = toTaskRequest(apiRequest);
        TaskResponse response = taskUseCase.createTask(taskRequest, userId);
        return new ResponseEntity<>(toTaskApiResponse(response), HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<TaskApiResponse>> getAllTasks(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<TaskResponse> tasks = taskUseCase.getAllTasks(userId);
        List<TaskApiResponse> apiResponses = tasks.stream()
                .map(this::toTaskApiResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiResponses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaskApiResponse> getTaskById(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskResponse response = taskUseCase.getTaskById(id, userId);
        return ResponseEntity.ok(toTaskApiResponse(response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TaskApiResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskApiRequest apiRequest,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskRequest taskRequest = toTaskRequest(apiRequest);
        TaskResponse response = taskUseCase.updateTask(id, taskRequest, userId);
        return ResponseEntity.ok(toTaskApiResponse(response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        taskUseCase.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/start")
    public ResponseEntity<TaskApiResponse> startTask(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskResponse response = taskUseCase.startTask(id, userId);
        return ResponseEntity.ok(toTaskApiResponse(response));
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskApiResponse> completeTask(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskResponse response = taskUseCase.completeTask(id, userId);
        return ResponseEntity.ok(toTaskApiResponse(response));
    }
    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TaskApiResponse> cancelTask(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskResponse response = taskUseCase.cancelTask(id, userId);
        return ResponseEntity.ok(toTaskApiResponse(response));
    }
    
    @PatchMapping("/{id}/reopen")
    public ResponseEntity<TaskApiResponse> reopenTask(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        TaskResponse response = taskUseCase.reopenTask(id, userId);
        return ResponseEntity.ok(toTaskApiResponse(response));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskApiResponse>> getTasksByStatus(
            @PathVariable String status,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<TaskResponse> tasks = taskUseCase.getTasksByStatus(status, userId);
        List<TaskApiResponse> apiResponses = tasks.stream()
                .map(this::toTaskApiResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiResponses);
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskApiResponse>> getTasksByPriority(
            @PathVariable String priority,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<TaskResponse> tasks = taskUseCase.getTasksByPriority(priority, userId);
        List<TaskApiResponse> apiResponses = tasks.stream()
                .map(this::toTaskApiResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiResponses);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskApiResponse>> getOverdueTasks(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<TaskResponse> tasks = taskUseCase.getOverdueTasks(userId);
        List<TaskApiResponse> apiResponses = tasks.stream()
                .map(this::toTaskApiResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiResponses);
    }
    
    // Métodos auxiliares
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUserId(token);
        }
        throw new SecurityException("Token no válido");
    }
    
    private TaskRequest toTaskRequest(TaskApiRequest apiRequest) {
        return new TaskRequest(
            apiRequest.getTitle(),
            apiRequest.getDescription(),
            apiRequest.getPriority(),
            apiRequest.getDueDate()
        );
    }
    
    private TaskApiResponse toTaskApiResponse(TaskResponse response) {
        TaskApiResponse apiResponse = new TaskApiResponse();
        apiResponse.setId(response.getId());
        apiResponse.setTitle(response.getTitle());
        apiResponse.setDescription(response.getDescription());
        apiResponse.setStatus(response.getStatus());
        apiResponse.setPriority(response.getPriority());
        apiResponse.setDueDate(response.getDueDate());
        apiResponse.setCreatedAt(response.getCreatedAt());
        apiResponse.setUpdatedAt(response.getUpdatedAt());
        apiResponse.setUserId(response.getUserId());
        apiResponse.setOverdue(response.isOverdue());
        return apiResponse;
    }
}