package com.hrsystem.employee.exception;

import com.hrsystem.employee.response.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadSqlGrammar(BadSqlGrammarException ex) {
        String message = ex.getMessage();
        String sql = extractSqlFromMessage(message);

        String errorMessage = "Table or relation does not exist in the current tenant schema. Please pass a valid tenantId in the request header.";
        String fullMessage = String.format("%s | SQL: %s", errorMessage, sql);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "TableNotFound", fullMessage));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccess(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DatabaseError", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError", ex.getMessage()));
    }

    private String extractSqlFromMessage(String msg) {
        int start = msg.indexOf('[');
        int end = msg.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return msg.substring(start + 1, end);
        }
        return "Unknown SQL statement";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() == null ? "Invalid" : fe.getDefaultMessage(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "ValidationFailed", "Validation failed: " + errors.toString()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataNotFound(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "DataNotFound", ex.getMessage()));
    }
}
