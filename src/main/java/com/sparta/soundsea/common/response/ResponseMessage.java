package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // User
    SIGNUP_USER_SUCCESS_MSG(200, "회원 가입 성공"),
    LOGIN_USER_SUCCESS_MSG(200, "유저 로그인 성공"),


    COMMENT_CREATE_SUCCESS_MSG(200,"댓글 작성 성공"),

    COMMENT_UPDATE_SUCCESS_MSG(200, "댓글 수정 성공"),


    COMMENT_DELETE_SUCCESS_MSG(200, "댓글 삭제 성공"),


    READ_MUSIC_ALL_SUCCESS_MSG(200, "추천 음악 전체 조회 성공"),

    READ_MUSIC_ONE_SUCCESS_MSG(200, "선택 추천 음악 조회 성공"),

    CREATE_MUSIC_SUCCESS_MSG(201, "추천 음악을 작성하였습니다."),

    UPDATE_MUSIC_SUCCESS_MSG(201,"추천 음악을 수정하였습니다."),

    DELETE_MUSIC_SUCCESS_MSG(200,"추천 음악을 삭제하였습니다.");


    private final int statusCode;
    private final String msg;
    ResponseMessage(final int status, final String msg) {
        this.statusCode = status;
        this.msg = msg;
    }
}
