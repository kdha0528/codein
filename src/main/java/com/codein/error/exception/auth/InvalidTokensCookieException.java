package com.codein.error.exception.auth;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class InvalidTokensCookieException extends CodeinException {
    public InvalidTokensCookieException() {
        super(ErrorCode.INVALID_TOKENS_COOKIE);
    }
}
