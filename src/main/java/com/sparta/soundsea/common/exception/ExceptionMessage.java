package com.sparta.soundsea.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

    // common
    INTERNAL_SERVER_ERROR_MSG(500,"서버 에러입니다."),

    // User
    ADMIN_TOKEN_MISMATCH_ERROR_MSG(400,"관리자 암호가 틀려 등록이 불가능합니다."),
    DUPLICATE_USER_ERROR_MSG(409,"중복된 사용자가 존재합니다."),
    USER_NOT_FOUND_ERROR_MSG(400,"회원을 찾을 수 없습니다!"),
    PASSWORDS_DO_NOT_MATCH_ERROR_MSG(400,"아이디 혹은 비밀번호가 일치하지 않습니다!"),
    INVALID_LOGINID_MSG(400,"유효하지 않은 ID 입니다."),
    INVALID_PASSWORD_MSG(400,"유효하지 않은 PW 입니다."),

    SOCIAL_LOGIN_ERROR_MSG(401, "카카오 소셜 로그인 오류입니다."),

    // JWT, OAuth
    TOKEN_NOT_FOUND_MSG(401,"토큰이 존재하지 않습니다."),
    INVALID_TOKEN_MSG(401,"토큰이 유효하지 않습니다."),
    UNAUTHORIZED_USER(401, "인가되지 않은 사용자입니다"),
    INVALID_AUTH_TOKEN(401,"권한이 없는 사용자 입니다"),
    REFRESH_TOKEN_NOT_FOUND_MSG(401, "로그아웃된 사용자입니다."),

    EXPIRATION_TOKEN(403, "Access Token이 만료되었습니다"),

    MUSIC_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),

    COMMENT_NOT_FOUND(404, "해당 댓글을 찾을 수 없습니다.");

    private final int statusCode;
    private final String msg;

    ExceptionMessage(final int statusCode, final String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
