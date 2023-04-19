package com.codein.error.exception.auth;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class AccessTokenNullException extends CodeinException {
    public AccessTokenNullException() {
        super(ErrorCode.ACCESS_TOKEN_NULL);
    }
}
