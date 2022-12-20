package com.sparta.soundsea.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NaverLoginDto {
    private String loginId;
    private String nickname;

    @Builder
    public NaverLoginDto(String loginId, String nickname) {
        this.loginId = loginId;
        this.nickname = nickname;
    }
}
