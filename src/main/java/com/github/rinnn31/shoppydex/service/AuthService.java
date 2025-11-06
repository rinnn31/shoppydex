package com.github.rinnn31.shoppydex.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.exception.UserNotFoundException;
import com.github.rinnn31.shoppydex.model.api.AuthInfoModel;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.model.entity.VerificationInfoEntity;
import com.github.rinnn31.shoppydex.repository.UserRepository;
import com.github.rinnn31.shoppydex.security.JwtTokenService;
import com.github.rinnn31.shoppydex.security.SPDPasswordEncoder;

@Service
public class AuthService {
    private static final String ACTION_VERIFY_EMAIL = "VERIFY_EMAIL";

    private static final String ACTION_RESET_PASSWORD = "RESET_PASSWORD";

    private static final int DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES = 2;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private SPDPasswordEncoder passwordEncoder;

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
        newUser.setRole(UserEntity.ROLE_USER);
        userRepository.save(newUser);
    }

    public String sendVerificationEmail(String identifier) {
        UserEntity user = userRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(() -> new UserNotFoundException(identifier));
        if (user.isVerified()) {
            throw new SPDException(104, "Email đã được xác minh");
        }
        // Kiểm tra xem có yêu cầu xác minh nào gần đây không, chỉ cho phép gửi lại sau một khoảng thời gian
        Optional<VerificationInfoEntity> existingVerification = verificationService.getLatestValidVerificationToken(user.getUsername(), ACTION_VERIFY_EMAIL);
        if (existingVerification.isPresent() && existingVerification.get().getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES))) {
            LocalDateTime nextAllowedTime = existingVerification.get().getCreatedAt().plusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES);
            Duration remainingSeconds = Duration.between(LocalDateTime.now(), nextAllowedTime);
            throw new SPDException(102, "Vui lòng chờ " + remainingSeconds.toSeconds() + " giây để gửi lại email xác minh");
        }

        String token = verificationService.createToken(user.getUsername(), ACTION_VERIFY_EMAIL);
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put("username", user.getUsername());
        templateData.put("verificationLink", "http://127.0.0.1:8080/auth/verify-email?token=" + token);
        templateData.put("expirationMinutes", String.valueOf(VerificationService.VERIFICATION_TOKEN_VALID_DURATION_MINUTES));
        mailService.sendTemplatedEmail(user.getEmail(), 
                               "Xác minh email tài khoản ShoppyDex", 
                          "private/mail-template/mail-verify.html", templateData);

        return user.getEmail();
    }


    public String sendPasswordResetEmail(String identifier) {
        UserEntity user = userRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(() -> new UserNotFoundException(identifier));

        // Kiểm tra xem có yêu cầu xác minh nào gần đây không, chỉ cho phép gửi lại sau một khoảng thời gian
        Optional<VerificationInfoEntity> existingVerification = verificationService.getLatestValidVerificationToken(user.getUsername(), ACTION_RESET_PASSWORD);
        System.out.println(existingVerification);
        if (existingVerification.isPresent() && existingVerification.get().getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES))) {
            LocalDateTime nextAllowedTime = existingVerification.get().getCreatedAt().plusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES);
            Duration remainingSeconds = Duration.between(LocalDateTime.now(), nextAllowedTime);
            throw new SPDException(102, "Vui lòng chờ " + remainingSeconds.toSeconds() + " giây để gửi lại email xác minh");
        }

        String token = verificationService.createToken(user.getUsername(), ACTION_RESET_PASSWORD);
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put("username", user.getUsername());
        templateData.put("resetLink", "http://127.0.0:8080/auth/reset-password?token=" + token);
        templateData.put("expirationMinutes", String.valueOf(VerificationService.VERIFICATION_TOKEN_VALID_DURATION_MINUTES));
        mailService.sendTemplatedEmail(user.getEmail(), 
                               "Đặt lại mật khẩu tài khoản ShoppyDex", 
                          "private/mail-template/mail-reset-password.html", templateData);
        
        return user.getEmail();
    }

    public void resetPassword(String username, String token, String newPassword) {
        UserEntity user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UserNotFoundException(username));

        if (!verificationService.verifyToken(username, token, ACTION_RESET_PASSWORD)) {
            throw new SPDException(103, "Token không hợp lệ hoặc đã hết hạn");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
