package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class PhoneAlreadyExistsException extends CodeinException {
    public PhoneAlreadyExistsException() {
        super(ErrorCode.PHONE_ALREADY_EXISTS);
    }
}
