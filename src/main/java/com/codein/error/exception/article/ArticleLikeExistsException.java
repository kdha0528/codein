package com.codein.error.exception.article;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class ArticleLikeExistsException extends CodeinException {
    public ArticleLikeExistsException() {
        super(ErrorCode.ARTICLE_LIKE_EXISTS);
    }

}
