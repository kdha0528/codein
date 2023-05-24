package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class DeletedArticleException extends CodeinException {
    public DeletedArticleException() {
        super(ErrorCode.DELETED_ARTICLE);
    }
}
