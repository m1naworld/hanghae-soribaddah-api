package com.sparta.soundsea.user.service;


import com.sparta.soundsea.security.jwt.JwtUtil;
import com.sparta.soundsea.user.dto.LoginUserDto;
import com.sparta.soundsea.user.dto.SignUpUserDto;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.mapper.UserMapper;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static com.sparta.soundsea.common.exception.ExceptionMessage.DUPLICATE_USER_ERROR_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.INVALID_LOGINID_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.INVALID_PASSWORD_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.USER_NOT_FOUND_ERROR_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.PASSWORDS_DO_NOT_MATCH_ERROR_MSG;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Validator validator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signUp(SignUpUserDto signUpUserDto) {
        // 1. LoginId 확인
        // 1-1. LoginId 형식 확인
        if (!validator.isValidMemberName(signUpUserDto.getLoginId())){
            throw new IllegalArgumentException(INVALID_LOGINID_MSG.getMsg());
        }
        // 1-2. LoginId 중복 확인
        userRepository.findByLoginId(signUpUserDto.getLoginId())
                .ifPresent( m-> {
                    throw new IllegalArgumentException(DUPLICATE_USER_ERROR_MSG.getMsg());
                });

        // 2. Password 확인
        // 2-1. Password 형식 확인
        if (!validator.isValidPassword(signUpUserDto.getPassword())){
            throw new IllegalArgumentException(INVALID_PASSWORD_MSG.getMsg());
        }

        // 3. Entity 생성 및 DB 저장
        User user = userMapper.toEntity(signUpUserDto);
        userRepository.save(user);
    }

    @Transactional
    public void login(LoginUserDto loginUserDto, HttpServletResponse response) {
        // LoginUserDto에서 확인할 Data들
        String loginId = loginUserDto.getLoginId();
        String password = loginUserDto.getPassword();

        // 1. 사용자 존재 여부 확인
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_ERROR_MSG.getMsg()));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException(PASSWORDS_DO_NOT_MATCH_ERROR_MSG.getMsg());
        }

        // 3. Social 로그인인지, 일반 로그인인지 확인
        // 3-1. Social 로그인일 경우
        if (user.getSocial()){
            // 4. OAuth 토큰 발행
        }

        if(!user.getSocial()){
            // 3-2. 일반 로그인인 경우
            // 4. JWT 토큰 발급
            // 4-1. Token 생성
            String accessToken = jwtUtil.createAccessToken(user.getLoginId(), user.getRole());
            String refreshToken = jwtUtil.createRefreshToken();

            // 4-2. Token 발급
            response.addHeader(JwtUtil.AUTHORIZATION_ACCESS, accessToken);
            response.addHeader(JwtUtil.AUTHORIZATION_REFRESH, refreshToken);

            // 5. 발급된 Token DB에 저장
            user.updateToken(accessToken, refreshToken);
        }
    }

}
