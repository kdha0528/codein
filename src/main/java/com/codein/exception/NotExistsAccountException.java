package com.codein.exception;

public class NotExistsAccountException extends CodeinException {
    private static final String MESSAGE = "존재하지 않는 계정입니다.";

    public NotExistsAccountException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
