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
    PAYLOAD_TOO_LARGE(400, "C007", "Payload is too large."),
    HANDLER_NOT_FOUND(404, "C007", "Handler is not founded."),

    // Image

    IMAGE_INVALID(400, "I001", "이미지 경로가 존재하지 않습니다."),

    // Auth
    ACCESS_TOKEN_NULL(401, "A001", "존재하지 않는 Accesstoken 입니다."),
    REFRESH_TOKEN_NULL(401, "A002", "존재하지 않는 Refreshtoken 입니다."),
    INVALID_ACCESS_TOKEN(401, "A003", "유효하지 않은 Access token 입니다."),
    INVALID_REFRESH_TOKEN(401, "A004", "유효하지 않은 Refresh token 입니다."),
    UNAUTHORIZED_ACCESS(403, "A005", "접근 권한이 부족합니다."),
    INVALID_TOKENS_COOKIE(401, "A006", "유효하지 않는 토큰입니다."),

    // Member
    EMAIL_ALREADY_EXISTS(400, "M001", "이미 존재하는 이메일입니다."),
    MISMATCH_LOGIN_INPUT(400, "M002", "로그인 정보가 일치하지 않습니다."),
    MEMBER_NOT_EXISTS(400, "M003", "존재하지 않는 멤버입니다."),
    MEMBER_NOT_LOGIN(400, "M004", "로그인하지 않은 멤버입니다."),
    PHONE_ALREADY_EXISTS(400, "M007", "이미 존재하는 전화번호입니다."),
    NICKNAME_ALREADY_EXISTS(400, "M008", "이미 존재하는 닉네임입니다."),

    // Article
    ARTICLE_NOT_EXISTS(400, "A001", "존재하지 않는 게시글입니다."),
    ARTICLE_LIKE_EXISTS(400, "A002", "이미 추천한 게시글입니다."),
    INVALID_AUTHOR(400,"A003", "게시글의 작성자가 아닙니다. 확인 후 다시 시도해주세요."),
    DELETED_ARTICLE(400, "A004", "삭제된 게시물입니다."),
    FREQUENT_WRITE_EXCEPTION(400, "A005", "연속으로 글을 작성할 수 없습니다. 잠시 후 다시 시도해주세요."),
    FREQUENT_LIKE_EXCEPTION(400, "A006", "추천/비추천은 10초에 1번씩 할 수 있습니다. 담시후 다시 시도해주세요."),

    // Comment
    COMMENT_NOT_EXISTS(400,"CMT001","존재하지 않는 댓글입니다."),
    ;
    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
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
