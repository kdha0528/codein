package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class MismatchLoginInputException extends CodeinException {

    public MismatchLoginInputException() {
        super(ErrorCode.MISMATCH_LOGIN_INPUT);
    }

}
