package com.sparta.soundsea.security;

import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.entity.UserRole;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 1. UserName이 Foreign인 경우(=로그인하지 않은 사용자인 경우 = 토큰이 비어있는 경우)
        if (userName.equals("Foreign")) {
            // 2. 임시로 사용할 User Foreign 생성 및 반환
            User foreign = new User(userName, null, null, false, UserRole.FOREIGN);
            return new CustomUserDetails(foreign, foreign.getLoginId(), foreign.getPassword());
        }

        // 사용자 조회
        User user = userRepository.findByLoginId(userName)
                .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // UserDetailsImpl 반환
        return new CustomUserDetails(user, user.getLoginId(), user.getPassword());
    }
}
