package com.db.bankingapi.exceptions;

import com.db.bankingapi.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IndividualNotFoundException.class)
    public ResponseEntity<GenericResponse> handleIndividualNotFoundException(IndividualNotFoundException ex){
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericResponse> handleRuntimeException(RuntimeException ex){
        return ResponseEntity.badRequest().body(new GenericResponse(ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new GenericResponse(ex.getMessage()));
    }
}
