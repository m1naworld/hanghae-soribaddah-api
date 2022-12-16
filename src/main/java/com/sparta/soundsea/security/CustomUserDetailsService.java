package com.sparta.soundsea.security;

import com.sparta.soundsea.user.entity.User;
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
        System.out.println("UserDetailsServiceImpl.loadUserByUsername : " + userName);

        // 사용자 조회
        User user = userRepository.findByLoginId(userName)
                .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // UserDetailsImpl 반환
        return new CustomUserDetails(user, user.getLoginId(), user.getPassword());
    }
}
