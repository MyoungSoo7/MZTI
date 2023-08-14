package com.example.mzti_server.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e instanceof SecurityException || e instanceof MalformedJwtException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다");
        } else if (e instanceof ExpiredJwtException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("만료된 토큰입니다");
        } else if (e instanceof UnsupportedJwtException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("지원되지 않는 토큰입니다");
        } else if (e instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    // runtimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleRuntimeException(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return getMapResponseEntity(responseHeaders, httpStatus, "[" + e.getMessage() + "]", "400");
    }

    // 유효성 검사
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        return getMapResponseEntity(responseHeaders, httpStatus, errorMessages.toString(), "406");
    }

    private ResponseEntity<LinkedHashMap<String, Object>> getMapResponseEntity(HttpHeaders responseHeaders, HttpStatus httpStatus, String message, String code) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("result_code", code);
        map.put("result_message", message);
        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}