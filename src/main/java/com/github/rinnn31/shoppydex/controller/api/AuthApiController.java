package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.AuthenticationDTO;
import com.github.rinnn31.shoppydex.model.api.LoginDTO;
import com.github.rinnn31.shoppydex.model.api.RegisterDTO;
import com.github.rinnn31.shoppydex.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private static final int AUTHENTICATION_ERROR_CODE = 101;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginDTO request) {
        AuthenticationDTO authDTO = authService.authenticate(request.getUsername(), request.getPassword());
        if (authDTO == null) {
            return ApiResponse.error(AUTHENTICATION_ERROR_CODE, "Tên đăng nhập hoặc mật khẩu không đúng");
        }
        return ApiResponse.success(authDTO);
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterDTO request) {
        authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return ApiResponse.success("Đăng ký tài khoản thành công");
    }

}
