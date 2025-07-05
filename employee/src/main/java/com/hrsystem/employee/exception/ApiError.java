package com.hrsystem.employee.exception;

import lombok.Data;

@Data
public class ApiError {
    private int status;
    private String error;
    private String message;
    private String details;

    public ApiError(int status, String error, String message, String details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }
}
