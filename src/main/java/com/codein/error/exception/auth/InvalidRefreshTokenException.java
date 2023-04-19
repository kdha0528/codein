package com.codein.error.exception.auth;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class InvalidRefreshTokenException extends CodeinException {
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
