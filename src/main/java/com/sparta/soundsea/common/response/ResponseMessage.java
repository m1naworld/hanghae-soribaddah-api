package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // 예시
    LOGIN_USER_SUCCESS_MSG(200, "유저 로그인 성공");

    private final int statusCode;
    private final String msg;
    ResponseMessage(final int status, final String msg) {
        this.statusCode = status;
        this.msg = msg;
    }
}
