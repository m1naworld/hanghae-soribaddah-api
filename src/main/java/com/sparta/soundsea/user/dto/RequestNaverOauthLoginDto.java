package com.sparta.soundsea.user.dto;

import lombok.Getter;
@Getter
public class RequestNaverOauthLoginDto {
    private String resultcode;
    private String message;
    private Response response;
    @Getter
    private static class Response {
        private String id;
        private String nickname;
        private String email;
        private String name;
    }

    public NaverLoginDto toNaverLoginDto(){
        return NaverLoginDto.builder()
                .loginId(response.email)
                .nickname(response.nickname)
                .build();
    }
}
