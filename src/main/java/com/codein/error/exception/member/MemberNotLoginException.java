package com.codein.error.exception.member;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class MemberNotLoginException extends CodeinException {
    public MemberNotLoginException() {
        super(ErrorCode.MEMBER_NOT_LOGIN);
    }
}
