package org.example.rgybackend.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ForbiddenException.class) 
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception e) {
        ErrorResponse error = new ErrorResponse(403, e.getMessage());
        System.out.println("403: " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotExistException.class) 
    public ResponseEntity<ErrorResponse> handleNotExistException(Exception e) {
        ErrorResponse error = new ErrorResponse(404, e.getMessage());
        System.out.println("404: " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse(500, e.getMessage());
        e.printStackTrace();
        System.out.println("500: " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
