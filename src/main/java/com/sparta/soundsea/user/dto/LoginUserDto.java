package com.sparta.soundsea.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUserDto {
    private String loginId;
    private String password;

    @Builder
    public LoginUserDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
