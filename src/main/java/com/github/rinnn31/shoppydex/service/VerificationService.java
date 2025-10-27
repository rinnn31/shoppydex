package com.github.rinnn31.shoppydex.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rinnn31.shoppydex.model.VerificationInfo;
import com.github.rinnn31.shoppydex.repository.VerificationRepository;

@Service
public class VerificationService {
    public static final int VERIFICATION_TOKEN_VALID_DURATION_MINUTES = 30;

    @Autowired
    private VerificationRepository verificationRepository;


    public boolean verifyToken(String username, String token, String action) {
        Optional<VerificationInfo> value = verificationRepository.findByVerificationToken(token);
        if (value.isEmpty()) {
            return false;
        }
        VerificationInfo verificationInfo = value.get();
        // Token chỉ hợp lệ nếu thuộc về đúng người dùng, đúng hành động và chưa hết hạn
        if (!verificationInfo.getAction().equals(action) || !verificationInfo.getUsername().equals(username) ||
            verificationInfo.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        verificationRepository.delete(verificationInfo);
        return true;
    }

    
    public String createToken(String username, String action) {
        VerificationInfo verificationInfo = new VerificationInfo(username, action, VERIFICATION_TOKEN_VALID_DURATION_MINUTES);
        verificationRepository.save(verificationInfo);
        return verificationInfo.getVerificationToken();
    }

    @Transactional
    public void deleteExpiredVerifications() {
        verificationRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }

    public Optional<VerificationInfo> getLatestValidVerificationToken(String username, String action) {
        return verificationRepository.findLatestValidByUserAndAction(username, action);
    }
}
