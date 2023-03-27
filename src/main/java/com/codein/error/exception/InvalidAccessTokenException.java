package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class InvalidAccessTokenException extends CodeinException {
    public InvalidAccessTokenException() {
        super(ErrorCode.ACCESS_TOKEN_INVALID);
    }
}
