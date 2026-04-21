package com.rto.license_services.exception;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles: .orElseThrow(() -> new ResourceNotFoundException(...))
    @ExceptionHandler(org.apache.kafka.common.errors.ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleKafkaNotFound(Exception ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 2. Handles: .orElseThrow(() -> new RuntimeException(...))
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        // We only want 404 if it's a "Not Found" message, otherwise keep it 500
        if (ex.getMessage().contains("not found") || ex.getMessage().contains("exist")) {
            return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return buildResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}