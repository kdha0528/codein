package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class ArticleNotExistsException extends CodeinException {

    public ArticleNotExistsException() {
        super(ErrorCode.ARTICLE_NOT_EXISTS);
    }
}
