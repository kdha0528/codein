package com.codein.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),


    // Member
    EMAIL_ALREADY_EXISTS(400, "M001", "Email is already exists"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    MEMBER_NOT_EXISTS(400, "M003", "Member is not exists"),
    MEMBER_NOT_LOGIN(400, "M004", "Member is not login yet"),
    UNAUTHORIZED_ACCESS(403, "M005", "Access is unauthorized"),
    ACCESS_TOKEN_INVALID(401, "M006", "Access token is invalid"),
    PHONE_ALREADY_EXISTS(400, "M007", "Phone is already exists"),
    // Post


    // Comment

    ;
    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}
