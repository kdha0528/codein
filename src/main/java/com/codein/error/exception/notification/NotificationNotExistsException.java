package com.codein.error.exception.notification;

import com.codein.error.ErrorCode;
import com.codein.error.exception.CodeinException;

public class NotificationNotExistsException extends CodeinException {
    public NotificationNotExistsException() {
        super(ErrorCode.NOTIFICATION_NOT_EXISTS);
    }
}
