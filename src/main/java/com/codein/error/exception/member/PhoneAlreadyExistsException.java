package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class PhoneAlreadyExistsException extends CodeinException {
    public PhoneAlreadyExistsException() {
        super(ErrorCode.PHONE_ALREADY_EXISTS);
    }
}
