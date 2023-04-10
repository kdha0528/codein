package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class ArticlePostNotExistsException extends CodeinException {

    public ArticlePostNotExistsException() {
        super(ErrorCode.ARTICLE_NOT_EXISTS);
    }
}
