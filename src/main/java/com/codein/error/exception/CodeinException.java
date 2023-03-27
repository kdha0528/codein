package com.codein.error.exception;

import com.codein.error.ErrorCode;

public abstract class CodeinException extends RuntimeException {

    private final ErrorCode errorCode;

    public CodeinException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
