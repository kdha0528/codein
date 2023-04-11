package com.codein.error.exception.profileimage;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class InvalidImageException extends CodeinException {
    public InvalidImageException() {
        super(ErrorCode.IMAGE_INVALID);
    }
}
