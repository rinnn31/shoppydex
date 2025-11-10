package com.github.rinnn31.shoppydex.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {
    @GetMapping("/dang-nhap")
    public String loginView() {
        return "login.html";
    }

    @GetMapping("/quen-mat-khau")
    public String forgotPasswordView() {
        return "reset-password.html";
    }
}
