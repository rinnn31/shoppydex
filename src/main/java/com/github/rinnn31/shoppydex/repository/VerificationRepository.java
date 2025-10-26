package com.github.rinnn31.shoppydex.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.model.VerificationInfo;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationInfo, Long> {
    Optional<VerificationInfo> findByVerificationToken(String verificationToken);

    Optional<VerificationInfo> findByUserAndAction(User user, String action);

    void deleteByExpiredAtBefore(java.time.LocalDateTime dateTime);

    boolean existsByUserAndExpiredAtAfter(User user, LocalDateTime dateTime);
}
