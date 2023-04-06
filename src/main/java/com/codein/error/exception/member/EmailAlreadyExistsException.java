package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class EmailAlreadyExistsException extends CodeinException {


    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

}
