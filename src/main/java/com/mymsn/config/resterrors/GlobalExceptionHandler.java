package com.mymsn.config.resterrors;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mymsn.exception.HttpErrorException;

@RestControllerAdvice
// To manage errors throw by server and make them readable in front side under a
// JSON format
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HttpErrorException.class)
    ProblemDetail handleBookmarkNotFoundException(HttpErrorException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getStatus(), e.getMessage());
        problemDetail.setTitle(e.getTitle());
        return problemDetail;
    }
}