package org.todoer.todoer.repositories;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.todoer.todoer.entities.Tag;
import org.todoer.todoer.util.Constants;

import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = Constants.TAG_CACHE)
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    @Cacheable(key = "#id")
    Optional<Tag> findById(Long id);

    @Cacheable(cacheNames = Constants.PROJECT_TAGS_CACHE, key = "#projectId")
    List<Tag> findByProjectId(Long projectId);

    @Cacheable(key = "'name-' + #name + '-project-' + #projectId")
    Optional<Tag> findByNameAndProjectId(String name, Long projectId);

    @Cacheable(key = "'with-tasks-' + #projectId")
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.tasks WHERE t.project.id = :projectId")
    List<Tag> findByProjectIdWithTasks(@Param("projectId") Long projectId);

    @CachePut(key = "#entity.id")
    @Override
    <S extends Tag> S save(S entity);

    @CacheEvict(value = {Constants.TAG_CACHE, Constants.PROJECT_TAGS_CACHE}, allEntries = true)
    @Override
    void deleteById(Long id);

    boolean existsByNameAndProjectId(String name, Long projectId);
}
