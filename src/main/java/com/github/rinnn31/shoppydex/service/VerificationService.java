package com.github.rinnn31.shoppydex.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rinnn31.shoppydex.model.entity.VerificationInfoEntity;
import com.github.rinnn31.shoppydex.repository.VerificationRepository;

@Service
public class VerificationService {
    public static final int VERIFICATION_CODE_VALID_DURATION_MINUTES = 30;

    @Autowired
    private VerificationRepository verificationRepository;

    public boolean verify(String username, String code, String action) {
        Optional<VerificationInfoEntity> value = verificationRepository.findByCode(code);
        System.out.println("Verification code found: " + value.isPresent());
        if (value.isEmpty()) {
            return false;
        }
        VerificationInfoEntity verificationInfo = value.get();
        // Token chỉ hợp lệ nếu thuộc về đúng người dùng, đúng hành động và chưa hết hạn
        if (!verificationInfo.getAction().equals(action) || !verificationInfo.getUsername().equals(username) ||
            verificationInfo.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        verificationRepository.delete(verificationInfo);
        return true;
    }

    @Transactional
    public String createVerificationSession(String username, String action) {
        // Xoá các mã xác thực cũ cùng loại của người dùng này
        verificationRepository.deleteByUsernameAndAction(username, action);
        VerificationInfoEntity verificationInfo = new VerificationInfoEntity(username, action, VERIFICATION_CODE_VALID_DURATION_MINUTES);
        verificationRepository.save(verificationInfo);
        return verificationInfo.getCode();
    }

    public void deleteExpiredVerifications() {
        verificationRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }

    public Optional<VerificationInfoEntity> getLatestValidVerificationCode(String username, String action) {
        return verificationRepository.findLatestValidByUserAndAction(username, action);
    }
}
