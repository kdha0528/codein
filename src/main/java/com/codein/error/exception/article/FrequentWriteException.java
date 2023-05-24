package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class FrequentWriteException extends CodeinException {
    public FrequentWriteException() {
        super(ErrorCode.FREQUENT_WRITE_EXCEPTION);
    }

}
