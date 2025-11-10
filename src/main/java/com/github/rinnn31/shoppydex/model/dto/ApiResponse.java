package com.github.rinnn31.shoppydex.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiResponse<T> {
    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, null, data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
