package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class InvalidImageException extends CodeinException {
    public InvalidImageException() {
        super(ErrorCode.IMAGE_INVALID);
    }
}
