package com.github.rinnn31.shoppydex.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.exception.UserNotFoundException;
import com.github.rinnn31.shoppydex.model.dto.AuthInfoModel;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.model.entity.VerificationInfoEntity;
import com.github.rinnn31.shoppydex.repository.UserRepository;
import com.github.rinnn31.shoppydex.security.JwtTokenService;

@Service
public class AuthService {
    private static final String ACTION_RESET_PASSWORD = "RESET_PASSWORD";

    private static final int DELAY_BETWEEN_VERIFICATION_EMAIL_SECONDS = 30;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private MailService mailService;

    public AuthInfoModel authenticate(String username, String password) {
        Optional<UserEntity> userOpt = userRepository.findByUsernameOrEmail(username, username);
        if (userOpt.isEmpty()) {
            return null;
        }
        UserEntity user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtTokenService.generateToken(user.getUsername());
            long expirationTime = jwtTokenService.extractExpiration(token).getTime();
            user.setActiveToken(token);
            userRepository.save(user);
            
            return new AuthInfoModel(token, user.getUsername(), expirationTime);
        } else {
            return null;
        }
    }

    public void register(String username, String email, String password) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new SPDException(100, "Tên đăng nhập hoặc email đã được sử dụng");
        }

        String encodedPassword = passwordEncoder.encode(password);
        UserEntity newUser = new UserEntity(username, email, encodedPassword);
        userRepository.save(newUser);
    }

    public void sendPasswordResetEmail(String identifier) {
        UserEntity user = userRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(() -> new UserNotFoundException(identifier));
        // Kiểm tra xem có yêu cầu xác minh nào gần đây không, chỉ cho phép gửi lại sau một khoảng thời gian
        Optional<VerificationInfoEntity> existingVerification = verificationService.getLatestValidVerificationCode(user.getUsername(), ACTION_RESET_PASSWORD);
        
        
        if (existingVerification.isPresent()) {
            // tính khoảng thời gian chênh lệch giữa 2 mốc thời gian
            long delta = Duration.between(existingVerification.get().getCreatedAt(), LocalDateTime.now()).toSeconds();
            // Nếu khoảng thời gian chênh lệch nhỏ hơn thời gian chờ định nghĩa, ném ngoại lệ
            if (delta < DELAY_BETWEEN_VERIFICATION_EMAIL_SECONDS) {
                long remainingSeconds = DELAY_BETWEEN_VERIFICATION_EMAIL_SECONDS - delta;
                throw new SPDException(102, String.format("Vui lòng chờ %d giây trước khi yêu cầu gửi lại email xác minh", remainingSeconds));
            }
        }

        String code = verificationService.createVerificationSession(user.getUsername(), ACTION_RESET_PASSWORD);
        
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put("username", user.getUsername());
        templateData.put("verification_code", code);
        templateData.put("expiry_minutes", String.valueOf(VerificationService.VERIFICATION_CODE_VALID_DURATION_MINUTES));
        mailService.sendTemplatedEmail(user.getEmail(), 
                               "Đặt lại mật khẩu tài khoản ShoppyDex", 
                          "static/template/reset-mail.html", templateData);
        
    }

    public void resetPassword(String username, String code, String newPassword) {
        UserEntity user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UserNotFoundException(username));

        if (!verificationService.verify(user.getUsername(), code, ACTION_RESET_PASSWORD)) {
            throw new SPDException(103, "Mã xác minh không hợp lệ hoặc đã hết hạn");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
