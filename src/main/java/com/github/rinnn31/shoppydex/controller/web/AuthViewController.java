package com.github.rinnn31.shoppydex.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.rinnn31.shoppydex.utils.WebViewRenderer;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthViewController {
    @GetMapping("/login")
    public void loginView(HttpServletResponse response) {
        WebViewRenderer.renderView(response, "private/login.html");
    }

    @GetMapping("/register")
    public void registerView(HttpServletResponse response) {
        WebViewRenderer.renderView(response, "private/register.html");
    }
}
