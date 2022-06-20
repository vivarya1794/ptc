package com.ptc.commons.exception;

import org.springframework.core.NestedRuntimeException;

public class BizException extends NestedRuntimeException {

    private String detailMessage;

    public BizException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BizException(final String detailMessage) {
        super(detailMessage);
        this.detailMessage = detailMessage;
    }

    public BizException(final String message, final String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}

