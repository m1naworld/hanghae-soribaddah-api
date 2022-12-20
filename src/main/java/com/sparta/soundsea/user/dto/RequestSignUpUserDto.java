package com.sparta.soundsea.user.dto;

import lombok.Getter;

@Getter
public class RequestSignUpUserDto {
    private String loginId;
    private String nickname;
    private String password;
    private boolean social = false;
    private boolean admin = false;
    private String adminToken = "";

    // Builder를 통해 RequestDTO를 ServiceDTO로 변경
    public SignUpUserDto toSignUpUserDto() {
        return SignUpUserDto.builder()
                .loginId(loginId)
                .nickname(nickname)
                .password(password)
                .social(social)
                .admin(admin)
                .adminToken(adminToken)
                .build();
    }
}
