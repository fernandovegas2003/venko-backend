package com.prueba.tecnica.backend.venko.domain.model;

import com.prueba.tecnica.backend.venko.domain.model.interfaces.ITask;
import java.time.LocalDateTime;

public class Task implements ITask {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    // Status constants
    public static final String STATUS_PENDING = "PENDIENTE";
    public static final String STATUS_IN_PROGRESS = "EN_PROGRESO";
    public static final String STATUS_COMPLETED = "COMPLETADA";
    public static final String STATUS_CANCELLED = "CANCELADA";

    // Priority constants
    public static final String PRIORITY_LOW = "BAJA";
    public static final String PRIORITY_MEDIUM = "MEDIA";
    public static final String PRIORITY_HIGH = "ALTA";
    public static final String PRIORITY_URGENT = "URGENTE";

    // Constructor para crear (registro)
    public Task(String title, String description, String priority, LocalDateTime dueDate, Long userId) {
        this.title = normalizeTitle(title);
        this.description = normalizeDescription(description);
        this.status = STATUS_PENDING;
        this.priority = (priority == null || priority.isBlank()) ? PRIORITY_MEDIUM : priority.trim();
        this.dueDate = dueDate;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;

        validateForCreation();
    }

    // Constructor para cargar desde BD
    public Task(Long id, String title, String description, String status, String priority,
                LocalDateTime dueDate, LocalDateTime createdAt, LocalDateTime updatedAt, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;

        validateStatus(this.status);
        validatePriority(this.priority);
    }

    // Para JPA/Hibernate
    protected Task() {}

    // ========== MÉTODOS PRIVADOS ==========
    private static String normalizeTitle(String title) {
        return title == null ? "" : title.trim();
    }

    private static String normalizeDescription(String description) {
        return description == null ? null : description.trim();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    private void validateStatus(String status) {
        if (status == null || (!STATUS_PENDING.equals(status) && !STATUS_IN_PROGRESS.equals(status) && !STATUS_COMPLETED.equals(status) && !STATUS_CANCELLED.equals(status))) {
            throw new IllegalArgumentException("Estado inválido");
        }
    }

    private void validatePriority(String priority) {
        if (priority == null || (!PRIORITY_LOW.equals(priority) && !PRIORITY_MEDIUM.equals(priority) && !PRIORITY_HIGH.equals(priority) && !PRIORITY_URGENT.equals(priority))) {
            throw new IllegalArgumentException("Prioridad inválida. Valores permitidos: BAJA, MEDIA, ALTA, URGENTE");
        }
    }

    public void validateForCreation() {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea es obligatorio");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("El título no puede exceder 100 caracteres");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
        }
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser en el pasado");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("El usuario asignado es obligatorio");
        }

        validateStatus(status);
        validatePriority(priority);
    }

    private void assertUpdatable() {
        if (STATUS_COMPLETED.equals(status) || STATUS_CANCELLED.equals(status)) {
            throw new IllegalStateException("No se puede modificar una tarea " +
                    (STATUS_COMPLETED.equals(status) ? "completada" : "cancelada"));
        }
    }

    // ========== IMPLEMENTACIÓN DE ITask ==========
    @Override
    public Long getId() { 
        return id; 
    }

    @Override
    public String getTitle() { 
        return title; 
    }

    @Override
    public String getDescription() { 
        return description; 
    }

    @Override
    public String getStatus() { 
        return status; 
    }

    @Override
    public String getPriority() { 
        return priority; 
    }

    @Override
    public LocalDateTime getDueDate() { 
        return dueDate; 
    }

    @Override
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    @Override
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }

    @Override
    public Long getUserId() { 
        return userId; 
    }

    @Override
    public void update(String title, String description, String priority, LocalDateTime dueDate) {
        assertUpdatable();

        if (title != null) {
            String t = normalizeTitle(title);
            if (!t.isEmpty()) {
                if (t.length() > 100) throw new IllegalArgumentException("El título no puede exceder 100 caracteres");
                this.title = t;
            }
        }

        if (description != null) {
            String d = normalizeDescription(description);
            if (d != null && d.length() > 500) {
                throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
            }
            this.description = d;
        }

        if (priority != null && !priority.isBlank()) {
            String p = priority.trim();
            validatePriority(p);
            this.priority = p;
        }

        if (dueDate != null) {
            if (dueDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("La fecha de vencimiento no puede ser en el pasado");
            }
            this.dueDate = dueDate;
        }

        touch();
    }

    @Override
    public void start() {
        if (STATUS_CANCELLED.equals(status)) throw new IllegalStateException("No se puede iniciar una tarea cancelada");
        if (STATUS_COMPLETED.equals(status)) throw new IllegalStateException("No se puede iniciar una tarea completada");
        if (!STATUS_IN_PROGRESS.equals(status)) {
            this.status = STATUS_IN_PROGRESS;
            touch();
        }
    }

    @Override
    public void complete() {
        if (STATUS_CANCELLED.equals(status)) throw new IllegalStateException("No se puede completar una tarea cancelada");
        if (!STATUS_COMPLETED.equals(status)) {
            this.status = STATUS_COMPLETED;
            touch();
        }
    }

    @Override
    public void cancel() {
        if (STATUS_COMPLETED.equals(status)) throw new IllegalStateException("No se puede cancelar una tarea completada");
        if (!STATUS_CANCELLED.equals(status)) {
            this.status = STATUS_CANCELLED;
            touch();
        }
    }

    @Override
    public void reopen() {
        if (STATUS_CANCELLED.equals(status)) {
            throw new IllegalStateException("No se puede reabrir una tarea cancelada");
        }
        if (STATUS_COMPLETED.equals(status)) {
            this.status = STATUS_PENDING;
            touch();
        }
    }

    @Override
    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) && !STATUS_COMPLETED.equals(status) && !STATUS_CANCELLED.equals(status);
    }

    @Override
    public boolean belongsToUser(Long userId) {
        return userId != null && userId.equals(this.userId);
    }

    @Override
    public boolean canBeDeleted() {
        return !STATUS_IN_PROGRESS.equals(status);
    }

    @Override
    public boolean isPending() { 
        return STATUS_PENDING.equals(status); 
    }

    @Override
    public boolean isInProgress() { 
        return STATUS_IN_PROGRESS.equals(status); 
    }

    @Override
    public boolean isCompleted() { 
        return STATUS_COMPLETED.equals(status); 
    }

    @Override
    public boolean isCancelled() { 
        return STATUS_CANCELLED.equals(status); 
    }

    // ========== SETTERS ==========
    public void setId(Long id) { 
        this.id = id; 
    }

    public void setTitle(String title) {
        String t = normalizeTitle(title);
        if (t.isEmpty() || t.length() > 100) {
            throw new IllegalArgumentException("Título inválido");
        }
        this.title = t;
        touch();
    }

    public void setDescription(String description) {
        String d = normalizeDescription(description);
        if (d != null && d.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
        }
        this.description = d;
        touch();
    }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = status;
        touch();
    }

    public void setPriority(String priority) {
        validatePriority(priority);
        this.priority = priority;
        touch();
    }

    public void setDueDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser en el pasado");
        }
        this.dueDate = dueDate;
        touch();
    }

    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }

    public void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("El usuario asignado es obligatorio");
        }
        this.userId = userId;
        touch();
    }

 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Task{id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", userId=" + userId +
                '}';
    }
}