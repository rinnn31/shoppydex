package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.ChangePasswordModel;
import com.github.rinnn31.shoppydex.service.UserService;

import jakarta.validation.Valid;

@RestController 
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/user")
public class UserApiController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ApiResponse<?> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.success(userService.getUserInfo(username));
    }

    @PostMapping("/change-password") 
    public ApiResponse<?> resetPassword(@Valid @RequestBody ChangePasswordModel changePasswordDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ApiResponse.success("Đổi mật khẩu thành công");
    }
}
