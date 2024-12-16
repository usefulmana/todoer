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
import org.todoer.todoer.entities.User;
import org.todoer.todoer.util.Constants;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = Constants.USER_CACHE)
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Cacheable(key = "#id")
    Optional<User> findById(Long id);

    @Cacheable(key = "#email")
    Optional<User> findByEmail(String email);

    @Cacheable(key = "#username")
    Optional<User> findByUsername(String username);

    @Cacheable(key = "'exists-email-' + #email")
    boolean existsByEmail(String email);

    @Cacheable(key = "'exists-username-' + #username")
    boolean existsByUsername(String username);

    @Cacheable(key = "'with-projects-' + #userId", condition = "#userId != null")
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.projects WHERE u.id = :userId")
    Optional<User> findByIdWithProjects(@Param("userId") Long userId);

    @CachePut(key = "#entity.id")
    @Override
    <S extends User> S save(S entity);

    @CacheEvict(allEntries = true)
    @Override
    void deleteById(Long id);
}
