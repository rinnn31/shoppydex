package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return ApiResponse.success(userService.getUserInfo(username));
    }

    @GetMapping("/send-mail-verification")
    public ApiResponse<?> sendVerification() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.sendVerificationEmail(username);
        return ApiResponse.success("Yêu cầu xác minh đã được gửi");
    }

    @GetMapping("/send-mail-reset-password")
    public ApiResponse<?> sendResetPassword() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.sendPasswordResetEmail(username);
        return ApiResponse.success("Yêu cầu đặt lại mật khẩu đã được gửi");
    }

    @PostMapping("/change-password") 
    public ApiResponse<?> resetPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.userUpdatePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ApiResponse.success("Đổi mật khẩu thành công");
    }

    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.adminUpdatePassword(username, resetPasswordDTO.getNewPassword(), resetPasswordDTO.getToken());
        return ApiResponse.success("Đặt lại mật khẩu thành công");
    }
}
