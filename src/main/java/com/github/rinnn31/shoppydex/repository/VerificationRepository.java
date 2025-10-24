package com.github.rinnn31.shoppydex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.VerificationInfo;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationInfo, Long> {
    VerificationInfo findByVerificationToken(String verificationToken);

    void deleteByVerificationToken(String verificationToken);

    void deleteByExpiredAtBefore(java.time.LocalDateTime dateTime);
}
