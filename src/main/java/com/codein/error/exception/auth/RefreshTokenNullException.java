package com.codein.error.exception.auth;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class RefreshTokenNullException extends CodeinException {
    public RefreshTokenNullException() {
        super(ErrorCode.REFRESH_TOKEN_NULL);
    }
}
