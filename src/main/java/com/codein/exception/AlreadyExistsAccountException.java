package com.codein.exception;

public class AlreadyExistsAccountException extends CodeinException {

    private static final String MESSAGE = "이미 존재하는 계정입니다.";

    public AlreadyExistsAccountException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
