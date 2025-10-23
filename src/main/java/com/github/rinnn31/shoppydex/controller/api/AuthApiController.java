package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.AuthenticationDTO;
import com.github.rinnn31.shoppydex.model.api.LoginDTO;
import com.github.rinnn31.shoppydex.model.api.RegisterDTO;
import com.github.rinnn31.shoppydex.security.JwtTokenService;
import com.github.rinnn31.shoppydex.security.SPDPasswordEncoder;
import com.github.rinnn31.shoppydex.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private static final int AUTHENTICATION_ERROR_CODE = 101;

    @Autowired
    private UserService accountService;

    @Autowired
    private SPDPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtGenerator;  

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginDTO request) {
        User account = accountService.getAccountByUserOrEmail(request.getUsername());
        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return ResponseEntity.ok(ApiResponse.error(AUTHENTICATION_ERROR_CODE, "Tên đăng nhập hoặc mật khẩu không đúng"));
        }

        String token = jwtGenerator.generateToken(account.getUsername());
        long expirationTime = jwtGenerator.extractExpiration(token).getTime();
        return ResponseEntity.ok(ApiResponse.success(new AuthenticationDTO(token, account.getUsername(), expirationTime)
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterDTO request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword()); 
        User account = accountService.createAccount(request.getUsername(), request.getEmail(), encodedPassword);
        String token = jwtGenerator.generateToken(account.getUsername());
        long expirationTime = jwtGenerator.extractExpiration(token).getTime();
        return ResponseEntity.ok(ApiResponse.success(new AuthenticationDTO(token, account.getUsername(), expirationTime)));
    }

}
