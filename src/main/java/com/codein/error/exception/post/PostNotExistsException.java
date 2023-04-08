package com.codein.error.exception.post;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class PostNotExistsException extends CodeinException {

    public PostNotExistsException() {
        super(ErrorCode.POST_NOT_EXISTS);
    }
}
