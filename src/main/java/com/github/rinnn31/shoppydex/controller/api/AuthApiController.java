package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.dto.ApiResponse;
import com.github.rinnn31.shoppydex.model.dto.AuthInfoModel;
import com.github.rinnn31.shoppydex.model.dto.LoginModel;
import com.github.rinnn31.shoppydex.model.dto.RegisterModel;
import com.github.rinnn31.shoppydex.model.dto.ResetPasswordModel;
import com.github.rinnn31.shoppydex.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private static final int AUTHENTICATION_ERROR_CODE = 101;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginModel request) {
        AuthInfoModel authDTO = authService.authenticate(request.getUsername(), request.getPassword());
        if (authDTO == null) {
            return ApiResponse.error(AUTHENTICATION_ERROR_CODE, "Tên đăng nhập hoặc mật khẩu không đúng");
        }
        return ApiResponse.success(authDTO);
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterModel request) {
        authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return ApiResponse.success("Đăng ký tài khoản thành công");
    }


    @GetMapping("/send-reset-code")
    public ApiResponse<?> sendResetPassword(@RequestParam("username") String username) {
        authService.sendPasswordResetEmail(username);
        return ApiResponse.success(null);
    }

    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@Valid @RequestBody ResetPasswordModel resetPasswordDTO) {
        authService.resetPassword(resetPasswordDTO.getUsername(), resetPasswordDTO.getCode(), resetPasswordDTO.getNewPassword());
        return ApiResponse.success("Đặt lại mật khẩu thành công");
    }

}
