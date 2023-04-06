package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class NicknameAlreadyExistsException extends CodeinException {
    public NicknameAlreadyExistsException() {
        super(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}
