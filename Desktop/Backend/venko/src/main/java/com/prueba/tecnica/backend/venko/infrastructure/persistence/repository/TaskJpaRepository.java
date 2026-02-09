package com.prueba.tecnica.backend.venko.infrastructure.persistence.repository;

import com.prueba.tecnica.backend.venko.infrastructure.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    
    List<TaskEntity> findByUserId(Long userId);
    
    List<TaskEntity> findByUserIdAndStatus(Long userId, String status);
    
    List<TaskEntity> findByUserIdAndPriority(Long userId, String priority);
    
    @Query("SELECT t FROM TaskEntity t WHERE t.userId = :userId AND t.dueDate < :now " +
           "AND t.status NOT IN ('COMPLETADA', 'CANCELADA')")
    List<TaskEntity> findOverdueTasksByUserId(@Param("userId") Long userId, 
                                             @Param("now") LocalDateTime now);
}