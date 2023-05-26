package com.codein.error.exception.comment;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class CommentNotExistsException  extends CodeinException {
    public CommentNotExistsException() {
        super(ErrorCode.COMMENT_NOT_EXISTS);
    }
}
