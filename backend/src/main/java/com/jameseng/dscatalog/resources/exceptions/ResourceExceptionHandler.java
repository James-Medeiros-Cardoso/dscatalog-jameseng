package com.jameseng.dscatalog.resources.exceptions;

import com.jameseng.dscatalog.services.exceptions.DatabaseException;
import com.jameseng.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice // vai interceptar exceções execeções na camada de resource
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // tipo de enxceção que ele vai interceptar
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        StandardError erro = new StandardError();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        erro.setTimestamp(Instant.now());
        erro.setStatus(httpStatus.value()); // .value() = pega o número inteiro
        erro.setError("Resource not found");
        erro.setMessage(e.getMessage());
        erro.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erro);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        StandardError erro = new StandardError();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        erro.setTimestamp(Instant.now());
        erro.setStatus(httpStatus.value());
        erro.setError("Database Integrity Violation");
        erro.setMessage(e.getMessage());
        erro.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erro);
    }

}