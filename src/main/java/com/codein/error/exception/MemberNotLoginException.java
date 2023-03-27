package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class MemberNotLoginException extends CodeinException {
    public MemberNotLoginException() {
        super(ErrorCode.MEMBER_NOT_LOGIN);
    }
}
