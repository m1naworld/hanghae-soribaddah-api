package com.sparta.soundsea.user.dto;

import lombok.Getter;

@Getter
public class RequestLoginUserDto {
    private String loginId;
    private String password;

    // Builder를 통해 RequestDto를 서비스 Dto로 변경
    public LoginUserDto toLoginUserDto() {
        return LoginUserDto.builder()
                .loginId(loginId)
                .password(password)
                .build();
    }
}
