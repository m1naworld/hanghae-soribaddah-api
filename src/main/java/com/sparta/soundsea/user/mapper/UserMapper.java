package com.sparta.soundsea.user.mapper;

import com.sparta.soundsea.user.dto.SignUpUserDto;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.sparta.soundsea.common.exception.ExceptionMessage.ADMIN_TOKEN_MISMATCH_ERROR_MSG;

@Component
@PropertySource("classpath:security.properties")
@RequiredArgsConstructor
public class UserMapper {
    @Value("${admin.token}")
    String ADMIN_TOKEN;

    private final PasswordEncoder passwordEncoder;

    public User toEntity(SignUpUserDto signUpUserDto) throws IllegalArgumentException{
        UserRole role = UserRole.USER;

        // 1. ADMIN_TOKEN과 일치하는지 확인
        if (signUpUserDto.isAdmin()) {
            if(!signUpUserDto.getAdminToken().equals(ADMIN_TOKEN)) {
                // 에러가 정상적으로 터지는지 확인 필요
                throw new IllegalArgumentException(ADMIN_TOKEN_MISMATCH_ERROR_MSG.getMsg());
            }
            // 2. 일치할 경우 ADMIN으로 권한 설정
            role = UserRole.ADMIN;
        }

        // 2. Password 암호화
        String encryptedPassword = passwordEncoder.encode(signUpUserDto.getPassword());

        // 3. Entity 생성
        return User.builder()
                .loginId(signUpUserDto.getLoginId())
                .nickname(signUpUserDto.getNickname())
                .password(encryptedPassword)
                .social(signUpUserDto.isSocial())
                .role(role)
                .build();
    }
}
