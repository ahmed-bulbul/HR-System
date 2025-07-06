package com.hrsystem.employee.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private String message;
    private int status;      // Change from HttpStatus to int
    private String error;
    private T data;
    private LocalDateTime timestamp;

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = null;
        this.timestamp = LocalDateTime.now();
    }

    private ApiResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = null;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(int status,String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String error, String message) {
        return new ApiResponse<>(status, error, message);
    }
}
