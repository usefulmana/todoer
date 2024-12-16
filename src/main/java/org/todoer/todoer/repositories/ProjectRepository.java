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
import org.springframework.stereotype.Repository;
import org.todoer.todoer.entities.Project;
import org.todoer.todoer.util.Constants;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = Constants.PROJECT_CACHE)
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @Cacheable(key = "#id")
    Optional<Project> findById(Long id);

    @Cacheable(cacheNames = Constants.USER_PROJECTS_CACHE, key = "#ownerId")
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    @Cacheable(key = "'with-details-' + #projectId")
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tasks LEFT JOIN FETCH p.tags WHERE p.id = :projectId")
    Optional<Project> findByIdWithDetails(@Param("projectId") Long projectId);

    @Cacheable(key = "'active-' + #userId + '-' + #date.toEpochSecond()")
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.tasks t " +
            "WHERE p.owner.id = :userId AND (t.dueDate IS NULL OR t.dueDate >= :date)")
    List<Project> findActiveProjectsByUserId(@Param("userId") Long userId, @Param("date") ZonedDateTime date);

    @CachePut(key = "#entity.id")
    @Override
    <S extends Project> S save(S entity);

    @CacheEvict(value = {Constants.PROJECT_CACHE, Constants.USER_PROJECTS_CACHE}, allEntries = true)
    @Override
    void deleteById(Long id);
}
