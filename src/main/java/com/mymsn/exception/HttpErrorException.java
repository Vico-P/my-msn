package com.mymsn.exception;

import org.springframework.http.HttpStatus;

// Exception raise to notify front of an error
public class HttpErrorException extends RuntimeException {
    private HttpStatus status;
    private String title;
    private String details;

    public HttpErrorException(HttpStatus status, String title, String details) {
        super(details);
        this.status = status;
        this.title = title;
        this.details = details;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
