package com.example.digital_fit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.digital_fit.exception.UsernameYaExiste;
import com.example.digital_fit.exception.EmailYaExisteException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameYaExiste.class)
    public ResponseEntity<String> handleUsernameException(UsernameYaExiste ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailYaExisteException.class)
    public ResponseEntity<String> handleEmailException(EmailYaExisteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
