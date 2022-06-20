package com.ptc.commons.exception;

import org.springframework.core.NestedRuntimeException;

public class ValidationException extends NestedRuntimeException {

    private String detailMessage;

    public ValidationException(final String message) {
        super(message);
        this.detailMessage = message;
    }

    public ValidationException(final String message, final String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
        this.detailMessage = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
