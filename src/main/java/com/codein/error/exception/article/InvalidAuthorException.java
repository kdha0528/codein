package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class InvalidAuthorException extends CodeinException {
    public InvalidAuthorException() {
        super(ErrorCode.INVALID_AUTHOR);
    }
}
