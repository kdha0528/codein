package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class NicknameAlreadyExistsException extends CodeinException {
    public NicknameAlreadyExistsException() {
        super(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}
