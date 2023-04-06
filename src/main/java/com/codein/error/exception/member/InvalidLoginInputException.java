package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class InvalidLoginInputException extends CodeinException {

    public InvalidLoginInputException() {
        super(ErrorCode.LOGIN_INPUT_INVALID);
    }

}
