package com.example.digital_fit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.digital_fit.exception.UsernameYaExiste;
import com.example.digital_fit.exception.EmailYaExisteException;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.exception.OperacionNoPermitida;

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

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(OperacionNoPermitida.class)
    public ResponseEntity<String> handleOperacionNoPermitida(OperacionNoPermitida ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}
