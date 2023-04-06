package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class EmailAlreadyExistsException extends CodeinException {


    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

}
