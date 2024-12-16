package org.todoer.todoer.repositories;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.todoer.todoer.entities.Task;
import org.todoer.todoer.entities.TaskStatus;
import org.todoer.todoer.util.Constants;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = Constants.TASK_CACHE)
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Cacheable(key = "#id")
    Optional<Task> findById(Long id);

    @Cacheable(cacheNames = Constants.PROJECT_TASKS_CACHE, key = "#projectId")
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    @Cacheable(key = "'assigned-' + #userId")
    Page<Task> findByAssignedToId(Long userId, Pageable pageable);

    @Cacheable(key = "'project-' + #projectId + '-status-' + #status")
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.status = :status")
    Page<Task> findByProjectAndStatus(
            @Param("projectId") Long projectId,
            @Param("status") TaskStatus status,
            Pageable pageable
    );

    @Cacheable(key = "'overdue-' + #date.toEpochSecond()")
    @Query("SELECT t FROM Task t WHERE t.dueDate <= :date AND t.status != 'DONE'")
    List<Task> findOverdueTasks(@Param("date") ZonedDateTime date);

    @CachePut(key = "#entity.id")
    @Override
    <S extends Task> S save(S entity);

    @CacheEvict(value = {Constants.TASK_CACHE, Constants.PROJECT_TASKS_CACHE}, allEntries = true)
    @Override
    void deleteById(Long id);

    @Scheduled(fixedRate = 3600000) // Every hour
    @CacheEvict(value = {Constants.TASK_CACHE, Constants.PROJECT_TASKS_CACHE}, allEntries = true)
    default void clearTaskCache() {

    }
}
