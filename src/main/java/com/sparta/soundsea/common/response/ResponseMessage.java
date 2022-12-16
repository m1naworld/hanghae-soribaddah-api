package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // 예시
    LOGIN_USER_SUCCESS_MSG(200, "유저 로그인 성공");

    private final int status;
    private final String msg;
    ResponseMessage(final int status, final String msg) {
        this.status = status;
        this.msg = msg;
    }
}
