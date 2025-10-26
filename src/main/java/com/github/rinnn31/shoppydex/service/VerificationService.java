package com.github.rinnn31.shoppydex.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.UserNotFoundException;
import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.model.VerificationInfo;
import com.github.rinnn31.shoppydex.repository.UserRepository;
import com.github.rinnn31.shoppydex.repository.VerificationRepository;

@Service
public class VerificationService {
    private static final int VERIFICATION_TOKEN_VALID_DURATION_MINUTES = 30;

    @Autowired
    private VerificationRepository verificationRepository;
    
    @Autowired
    private UserRepository userRepository;


    public boolean verifyToken(String token, String action) {
        Optional<VerificationInfo> value = verificationRepository.findByVerificationToken(token);
        if (value.isEmpty()) {
            return false;
        }
        VerificationInfo verificationInfo = value.get();
        // Token chỉ hợp lệ nếu action khớp và chưa hết hạn
        if (!verificationInfo.getAction().equals(action) || verificationInfo.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        verificationRepository.delete(verificationInfo);
        return true;
    }

    public String createToken(String username, String action) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        VerificationInfo verificationInfo = new VerificationInfo(user.get(), action, VERIFICATION_TOKEN_VALID_DURATION_MINUTES);
        verificationRepository.save(verificationInfo);
        return verificationInfo.getVerificationToken();
    }

    public void deleteExpiredVerifications() {
        verificationRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }

}
