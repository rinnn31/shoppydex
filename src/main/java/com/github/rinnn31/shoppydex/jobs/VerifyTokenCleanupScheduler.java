package com.github.rinnn31.shoppydex.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.rinnn31.shoppydex.service.VerificationService;

@Component
public class VerifyTokenCleanupScheduler {
    
    @Autowired
    private VerificationService verificationService;

    @Scheduled(fixedDelay=3600000) 
    public void cleanupExpiredTokens() {
        verificationService.deleteExpiredVerifications();
    }
}
