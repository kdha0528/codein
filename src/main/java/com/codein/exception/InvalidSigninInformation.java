package com.codein.exception;

public class InvalidSigninInformation extends CodeinException {

    private static final String MESSAGE = "이메일/비밀번호가 올바르지 않습니다.";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
