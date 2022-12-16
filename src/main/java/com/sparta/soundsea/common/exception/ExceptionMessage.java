package com.sparta.soundsea.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

    // common
    INTERNAL_SERVER_ERROR_MSG(500,"서버 에러입니다."),

    // 403 토큰 만료
    EXPIRATION_TOKEN(403, "Access Token이 만료되었습니다"),

    // 403 권한 없음
    INVALID_AUTH_TOKEN(403,"권한이 없는 사용자 입니다"),

    // 404 해당댓글 없음
    COMMENT_NOT_FOUND(404, "해당 댓글을 찾을 수 없습니다.");

    private final int statusCode;
    private final String msg;

    ExceptionMessage(final int statusCode, final String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
