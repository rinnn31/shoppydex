package com.github.rinnn31.shoppydex.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RESTExceptionHandler {
    private static final int VALIDATION_ERROR_CODE = 100;

    private static final int GENERIC_ERROR_CODE = -1;

    private static final Logger logger = LoggerFactory.getLogger(RESTExceptionHandler.class);

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
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(500).body(ApiResponse.error(GENERIC_ERROR_CODE, "Lỗi không xác định"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Xác minh dữ liệu thất bại");

        return ResponseEntity.badRequest().body(ApiResponse.error(VALIDATION_ERROR_CODE, message));
    }

    @ExceptionHandler(SPDException.class)
    public ApiResponse<?> handleSPDException(SPDException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

}
