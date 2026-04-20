package com.dbo.OrderManagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<ErrorResponse> HandlerOrderNotFound(OrderNotFoundException ex) {

        ErrorResponse error=new ErrorResponse(ex.getMessage(), 404);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> HandlerGeneric(Exception ex){



            ErrorResponse error=new ErrorResponse(ex.getMessage(),500);

            return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}
