package com.github.rinnn31.shoppydex.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.rinnn31.shoppydex.repository.VerificationRepository;

@Component
public class TokenCleanupScheduler {
    
    @Autowired
    private VerificationRepository verificationRepository;

    @Scheduled(fixedDelay=180000) 
    public void cleanupExpiredTokens() {
        verificationRepository.deleteByExpiredAtBefore(java.time.LocalDateTime.now());
    }
}
