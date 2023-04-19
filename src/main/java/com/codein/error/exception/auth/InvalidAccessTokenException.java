package com.codein.error.exception.auth;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class InvalidAccessTokenException extends CodeinException {
    public InvalidAccessTokenException() {
        super(ErrorCode.INVALID_ACCESS_TOKEN);
    }
}
