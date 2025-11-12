package com.github.rinnn31.shoppydex.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.entity.VerificationInfoEntity;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationInfoEntity, Long> {
    Optional<VerificationInfoEntity> findByUsernameAndAction(String username, String action);

    @Query("SELECT v FROM VerificationInfo v WHERE v.username = :username AND v.action = :action AND v.expiredAt > CURRENT_TIMESTAMP ORDER BY v.createdAt DESC LIMIT 1")
    Optional<VerificationInfoEntity> findLatestValidByUserAndAction(String username, String action);

    void deleteByExpiredAtBefore(java.time.LocalDateTime dateTime);

    void deleteByUsernameAndAction(String username, String action);

    boolean existsByUsernameAndExpiredAtAfter(String username, LocalDateTime dateTime);
}
