package com.github.rinnn31.shoppydex.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.github.rinnn31.shoppydex.utils.WebViewRenderer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SPDAccessHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/api/")) {
            writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access to API");
        } else {
            writeHtmlResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "private/401.html");
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/api/")) {
            writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden access to API");       
        } else {
            writeHtmlResponse(response, HttpServletResponse.SC_FORBIDDEN, "private/403.html");
        }
    }

    private void writeHtmlResponse(HttpServletResponse response, int status, String outHtmlPage) {
        response.setStatus(status);
        WebViewRenderer.renderView(response, outHtmlPage);
    }

    private void writeJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": %d, \"message\": \"%s\"}", status, message));
    }
    
}