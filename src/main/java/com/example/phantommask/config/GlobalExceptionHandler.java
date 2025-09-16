package com.example.phantommask.config;

import com.example.phantommask.param.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.builder().
                    msg(ex.getMessage()).
                    code("500").
                    build()
                , HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.builder().
                        msg(ex.getMessage()).
                        code("400").
                        build()
                , HttpStatus.OK);
    }
}
