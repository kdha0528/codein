package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class MemberNotExistsException extends CodeinException {

    public MemberNotExistsException() {
        super(ErrorCode.MEMBER_NOT_EXISTS);
    }

}
