package com.github.rinnn31.shoppydex.controller.advice;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.dto.ApiResponse;
import com.github.rinnn31.shoppydex.utils.HtmlRenderer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandler {
    private static final int VALIDATION_ERROR_CODE = 100;

    private static final int GENERIC_ERROR_CODE = -1;

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : null;
        if (message == null || message.isEmpty()) {
            message = "Xác minh dữ liệu thất bại";
        }

        return ResponseEntity.badRequest().body(ApiResponse.error(VALIDATION_ERROR_CODE, message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Xác minh dữ liệu thất bại");

        return ResponseEntity.badRequest().body(ApiResponse.error(VALIDATION_ERROR_CODE, message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingParamsException(MissingServletRequestParameterException ex) {
        String message = String.format("Thiếu tham số yêu cầu: %s", ex.getParameterName());
        return ResponseEntity.badRequest().body(ApiResponse.error(VALIDATION_ERROR_CODE, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        LOGGER.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(500).body(ApiResponse.error(GENERIC_ERROR_CODE, "Lỗi không xác định"));
    }

    @ExceptionHandler(SPDException.class)
    public ResponseEntity<ApiResponse<?>> handleSPDException(SPDException ex) {
        return ResponseEntity.ok(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFoundException(HttpServletRequest request, HttpServletResponse response) {
        if (request.getRequestURI().startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            try {
                response.getWriter().write("{\"error\": 404, \"message\": \"Resource not found\"}");
            } catch (IOException e) {
                LOGGER.error("Error writing JSON response for 404", e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            HtmlRenderer.renderView(response, "static/404.html");
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response) {
        if (request.getRequestURI().startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            try {
                response.getWriter().write("{\"error\": 401, \"message\": \"Unauthorized access to API\"}");
            } catch (IOException e) {
                LOGGER.error("Error writing JSON response for 401", e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            HtmlRenderer.renderView(response, "static/401.html");
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response) {
        System.out.println( request.getRequestURI());        
        if (request.getRequestURI().startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            try {
                response.getWriter().write("{\"error\": 403, \"message\": \"Forbidden access to API\"}");
            } catch (IOException e) {
                LOGGER.error("Error writing JSON response for 403", e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            HtmlRenderer.renderView(response, "static/403.html");
        }
    }

}
