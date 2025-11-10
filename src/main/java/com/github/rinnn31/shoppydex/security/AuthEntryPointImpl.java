package com.github.rinnn31.shoppydex.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.github.rinnn31.shoppydex.utils.HtmlRenderer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        if (request.getRequestURI().startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().write("{\"code\":401,\"message\":\"Vui lòng đăng nhập để sử dụng tính năng này\"}");
            } catch (IOException e) {
                // ignored
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            HtmlRenderer.renderView(response, "static/401.html");
        }

    }
}