package com.prueba.tecnica.backend.venko.domain.model.interfaces;

import java.time.LocalDateTime;

public interface ITask {
    
    // Métodos de acceso
    Long getId();
    String getTitle();
    String getDescription();
    String getStatus();
    String getPriority();
    LocalDateTime getDueDate();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Long getUserId();
    
    // Métodos de negocio
    void update(String title, String description, String priority, LocalDateTime dueDate);
    void start();
    void complete();
    void cancel();
    void reopen();
    
    // Métodos de consulta
    boolean isOverdue();
    boolean belongsToUser(Long userId);
    boolean canBeDeleted();
    
    // Métodos de estado
    boolean isPending();
    boolean isInProgress();
    boolean isCompleted();
    boolean isCancelled();
}