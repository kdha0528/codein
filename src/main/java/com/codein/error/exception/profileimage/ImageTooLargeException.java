package com.codein.error.exception.profileimage;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class ImageTooLargeException extends CodeinException {
    public ImageTooLargeException() {
        super(ErrorCode.PAYLOAD_TOO_LARGE);
    }
}
