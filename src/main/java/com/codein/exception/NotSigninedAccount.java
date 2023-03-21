package com.codein.exception;

public class NotSigninedAccount extends CodeinException {
    private static final String MESSAGE = "로그인하지 않은 계정입니다.";

    public NotSigninedAccount() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
