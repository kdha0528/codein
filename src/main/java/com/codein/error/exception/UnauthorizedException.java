package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class UnauthorizedException extends CodeinException {

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }

}
