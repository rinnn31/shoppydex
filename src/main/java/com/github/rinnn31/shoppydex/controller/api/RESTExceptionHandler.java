package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.service.ProductService.DuplicateResourceException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RESTExceptionHandler {
    private static final int VALIDATION_ERROR_CODE = 100;

    private static final int GENERIC_ERROR_CODE = -1;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : null;
        if (message == null || message.isEmpty()) {
            message = "Xác minh dữ liệu thất bại";
        }

        return ResponseEntity.badRequest().body(ApiResponse.error(VALIDATION_ERROR_CODE, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        ex.printStackTrace();   
        return ResponseEntity.status(500).body(ApiResponse.error(GENERIC_ERROR_CODE, "Lỗi máy chủ nội bộ: " + ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Xác minh dữ liệu thất bại");

        return ResponseEntity.badRequest().body(ApiResponse.error(VALIDATION_ERROR_CODE, message));
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateResourceException(DuplicateResourceException ex) {
        String message = ex.getMessage();
        return ResponseEntity.badRequest().body(ApiResponse.error(101, message));
    }

}
