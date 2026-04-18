package com.example.messageprovisioning.provisioning.model.exception;

import com.example.messageprovisioning.provisioning.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jboss.logging.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NumberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NumberNotFoundException ex, HttpServletRequest req){
        ErrorResponse errorResponse = new ErrorResponse(java.time.LocalDateTime.now(),404, "NOT_FOUND", ex.getMessage(),
                req.getRequestURI(), (String) MDC.get("correlationId"), null) ;

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);


    }

    @ExceptionHandler(NoNumberFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoNumber(NoNumberFoundException ex, HttpServletRequest req){
        ErrorResponse errorResponse = new ErrorResponse(java.time.LocalDateTime.now(), 409, "NO_NUMBER_AVAILABLE",
                ex.getMessage(), req.getRequestURI(), (String) MDC.get("correlationID"), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }



}
