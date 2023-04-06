package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class MemberNotExistsException extends CodeinException {

    public MemberNotExistsException() {
        super(ErrorCode.MEMBER_NOT_EXISTS);
    }

}
