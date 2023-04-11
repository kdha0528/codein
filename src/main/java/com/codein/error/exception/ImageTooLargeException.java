package com.codein.error.exception;

import com.codein.error.ErrorCode;

public class ImageTooLargeException extends CodeinException {
    public ImageTooLargeException() {
        super(ErrorCode.PAYLOAD_TOO_LARGE);
    }
}
