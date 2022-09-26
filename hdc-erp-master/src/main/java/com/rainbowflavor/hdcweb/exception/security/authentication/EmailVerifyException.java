package com.rainbowflavor.hdcweb.exception.security.authentication;


import org.springframework.security.core.AuthenticationException;

public class EmailVerifyException extends AuthenticationException {

    public EmailVerifyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailVerifyException(String msg) {
        super(msg);
    }
}
