package com.ptc.commons.dto.error;


import com.google.common.collect.Lists;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors = Lists.newArrayList();

    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors.addAll(errors);
    }

    public ApiError(final HttpStatus status, final String message, final String error) {
        this.status = status;
        this.message = message;
        this.errors.add(error);
    }

    public ApiError(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
        this.errors.add(message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }
}
