package com.sparta.soundsea.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthLoginDto {
    private String loginId;
    private String nickname;

    @Builder
    public OAuthLoginDto(String loginId, String nickname) {
        this.loginId = loginId;
        this.nickname = nickname;
    }
}
