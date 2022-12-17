package com.sparta.soundsea.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpUserDto {
    private String loginId;
    private String nickname;
    private String password;
    private boolean social;
    private boolean admin;
    private String adminToken;

    @Builder
    public SignUpUserDto(String loginId, String nickname, String password, boolean social, boolean admin, String adminToken) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
        this.social = social;
        this.admin = admin;
        this.adminToken = adminToken;
    }
}
