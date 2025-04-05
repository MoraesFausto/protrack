package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.edu.alunos.utfpr.protrack.domain.exceptions.BadRequestException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    static final String MESSAGE_STRING = "message";
    static final String TIMESTAMP_STRING = "timestamp";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(final NotFoundException ex) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE_STRING, ex.getMessage());
        body.put(TIMESTAMP_STRING, LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(final BadRequestException ex) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE_STRING, ex.getMessage());
        body.put(TIMESTAMP_STRING, LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(final Exception ex) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE_STRING, ex.getMessage());
        body.put(TIMESTAMP_STRING, LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
