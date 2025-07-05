package com.hrsystem.employee.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ApiError> handleBadSqlGrammar(BadSqlGrammarException ex) {
        String message = ex.getMessage();
        String sql = extractSqlFromMessage(message);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "TableNotFound",
                "Table or relation does not exist in the current tenant schema.Please pass a valid tenantId on the request header. ",
                sql
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccess(DataAccessException ex) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "DatabaseError",
                "A database error occurred.",
                ex.getMostSpecificCause().getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOtherExceptions(Exception ex) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "InternalServerError",
                "Unexpected server error.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String extractSqlFromMessage(String msg) {
        // Optional: try to extract SQL from the message
        int start = msg.indexOf('[');
        int end = msg.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return msg.substring(start + 1, end);
        }
        return "Unknown SQL statement";
    }
}
