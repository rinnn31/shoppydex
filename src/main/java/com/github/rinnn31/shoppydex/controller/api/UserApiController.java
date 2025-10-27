package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.ChangePasswordDTO;
import com.github.rinnn31.shoppydex.model.api.ResetPasswordDTO;
import com.github.rinnn31.shoppydex.service.UserService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/user")
public class UserApiController {
    @Autowired
    private UserService userService;


    @GetMapping("/info")
    public ApiResponse<?> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.success(userService.getUserInfo(username));
    }

    @GetMapping("/send-mail-verification")
    public ApiResponse<?> sendVerification() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.sendVerificationEmail(username);
        return ApiResponse.success("Yêu cầu xác minh đã được gửi");
    }

    @GetMapping("/send-mail-reset-password")
    public ApiResponse<?> sendResetPassword() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.sendPasswordResetEmail(username);
        return ApiResponse.success("Yêu cầu đặt lại mật khẩu đã được gửi");
    }

    @PostMapping("/change-password") 
    public ApiResponse<?> resetPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.userUpdatePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ApiResponse.success("Đổi mật khẩu thành công");
    }

    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.adminUpdatePassword(username, resetPasswordDTO.getNewPassword(), resetPasswordDTO.getToken());
        return ApiResponse.success("Đặt lại mật khẩu thành công");
    }
}
