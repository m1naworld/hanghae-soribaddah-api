package com.sparta.soundsea.user.service;

import com.sparta.soundsea.security.jwt.JwtUtil;
import com.sparta.soundsea.user.dto.NaverLoginDto;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.mapper.UserMapper;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNaverService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Transactional
    public void naverLogin(NaverLoginDto naverLoginDto){
        // 1. DB에 등록된 사용자인지 확인
        User naverUser = userRepository.findByLoginId(naverLoginDto.getLoginId())
                .orElse(null);

        // 2. 등록되지 않은 사용자인 경우 우선 DB에 저장
        if(naverUser == null) {
            // 2-1. naverLoginDto를 User Entity로 변환
            naverUser = userMapper.toEntityNaver(naverLoginDto);
            // 2-2. 사용자 저장
            userRepository.saveAndFlush(naverUser);
        }

        // 3. Token 발급
        String accessToken = jwtUtil.createAccessToken(naverUser.getLoginId(), naverUser.getRole());
        String refreshToken = jwtUtil.createRefreshToken();

        // 4. 발급된 Token DB에 저장
        naverUser.updateToken(accessToken, refreshToken);
    }
}
