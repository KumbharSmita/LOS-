//package com.imperacred.BankLoanApplication.exception;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<?> handleIOException(IOException ex) {
//        logger.error("IO Exception: {}", ex.getMessage());
//        return buildErrorResponse("File handling error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception ex) {
//        logger.error("Unhandled Exception: {}", ex.getMessage());
//        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("timestamp", LocalDateTime.now());
//        error.put("error", message);
//        error.put("status", status.value());
//        return new ResponseEntity<>(error, status);
//    }
//}
