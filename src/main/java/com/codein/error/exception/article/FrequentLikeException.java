package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class FrequentLikeException extends CodeinException {
    public FrequentLikeException() {
        super(ErrorCode.FREQUENT_LIKE_EXCEPTION);
    }

}
