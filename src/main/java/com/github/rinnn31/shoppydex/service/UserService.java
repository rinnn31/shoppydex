package com.github.rinnn31.shoppydex.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.exception.UserNotFoundException;
import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.model.VerificationInfo;
import com.github.rinnn31.shoppydex.model.api.UserDTO;
import com.github.rinnn31.shoppydex.repository.UserRepository;
import com.github.rinnn31.shoppydex.security.SPDPasswordEncoder;

@Service
public class UserService {
    private static final String ACTION_VERIFY_EMAIL = "VERIFY_EMAIL";

    private static final String ACTION_RESET_PASSWORD = "RESET_PASSWORD";

    private static final int DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES = 2;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SPDPasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private MailService mailService;

    public UserDTO getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));
        return new UserDTO(user);
    }   

    public void userUpdatePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));
        if (!passwordEncoder.matches(oldPassword,  user.getPassword())) {
            throw new SPDException(101, "Mật khẩu cũ không đúng");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void adminUpdatePassword(String username, String newPassword, String resetPasswordToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        if (!verificationService.verifyToken(username, resetPasswordToken, ACTION_RESET_PASSWORD)) {
            throw new SPDException(103, "Token không hợp lệ hoặc đã hết hạn");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void setUserPoints(String username, int points) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));
        user.setPoints(points);
        userRepository.save(user);
    }

    public void addUserPoints(String username, int pointsToAdd) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));
        user.setPoints(user.getPoints() + pointsToAdd);
        userRepository.save(user);
    }

    public void sendVerificationEmail(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // Kiểm tra xem có yêu cầu xác minh nào gần đây không, chỉ cho phép gửi lại sau một khoảng thời gian
        Optional<VerificationInfo> existingVerification = verificationService.getLatestValidVerificationToken(username, ACTION_VERIFY_EMAIL);
        if (existingVerification.isPresent() && existingVerification.get().getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES))) {
            LocalDateTime nextAllowedTime = existingVerification.get().getCreatedAt().plusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES);
            Duration remainingSeconds = Duration.between(LocalDateTime.now(), nextAllowedTime);
            throw new SPDException(102, "Vui lòng chờ " + remainingSeconds.toSeconds() + " giây để gửi lại email xác minh");
        }

        String token = verificationService.createToken(username, ACTION_VERIFY_EMAIL);
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put("username", user.getUsername());
        templateData.put("verificationLink", "http://127.0.0.1:8080/user/verify-email?token=" + token);
        templateData.put("expirationMinutes", String.valueOf(VerificationService.VERIFICATION_TOKEN_VALID_DURATION_MINUTES));
        mailService.sendTemplatedEmail(user.getEmail(), 
                               "Xác minh email tài khoản ShoppyDex", 
                          "private/verify_mail_template.html", templateData);
    }


    public void sendPasswordResetEmail(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        // Kiểm tra xem có yêu cầu xác minh nào gần đây không, chỉ cho phép gửi lại sau một khoảng thời gian
        Optional<VerificationInfo> existingVerification = verificationService.getLatestValidVerificationToken(username, ACTION_VERIFY_EMAIL);
        if (existingVerification.isPresent() && existingVerification.get().getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES))) {
            LocalDateTime nextAllowedTime = existingVerification.get().getCreatedAt().plusMinutes(DELAY_BETWEEN_VERIFICATION_EMAIL_MINUTES);
            Duration remainingSeconds = Duration.between(LocalDateTime.now(), nextAllowedTime);
            throw new SPDException(102, "Vui lòng chờ " + remainingSeconds.toSeconds() + " giây để gửi lại email xác minh");
        }

        String token = verificationService.createToken(username, ACTION_RESET_PASSWORD);
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put("username", user.getUsername());
        templateData.put("resetLink", "http://127.0.0:8080/user/reset-password?token=" + token);
        mailService.sendTemplatedEmail(user.getEmail(), 
                               "Đặt lại mật khẩu tài khoản ShoppyDex", 
                          "private/reset_password_template.html", templateData);
    }
}
