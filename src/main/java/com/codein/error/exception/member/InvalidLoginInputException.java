package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class InvalidLoginInputException extends CodeinException {

    public InvalidLoginInputException() {
        super(ErrorCode.LOGIN_INPUT_INVALID);
    }

}
