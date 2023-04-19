package com.codein.error.exception.auth;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class UnauthorizedException extends CodeinException {

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }

}
