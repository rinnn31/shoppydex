package com.github.rinnn31.shoppydex.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.VerificationInfo;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationInfo, Long> {
    Optional<VerificationInfo> findByVerificationToken(String verificationToken);

    @Query("SELECT v FROM VerificationInfo v WHERE v.username = :username AND v.action = :action AND v.expiredAt > CURRENT_TIMESTAMP ORDER BY v.createdAt DESC")
    Optional<VerificationInfo> findLatestValidByUserAndAction(String username, String action);

    void deleteByExpiredAtBefore(java.time.LocalDateTime dateTime);

    boolean existsByUserAndExpiredAtAfter(String username, LocalDateTime dateTime);
}
