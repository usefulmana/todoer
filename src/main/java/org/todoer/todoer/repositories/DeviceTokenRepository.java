package org.todoer.todoer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.todoer.todoer.entities.DeviceToken;

import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    List<DeviceToken> findByUserId(Long userId);
    void deleteByToken(String token);
}
