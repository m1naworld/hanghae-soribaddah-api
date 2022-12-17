package com.sparta.soundsea.security;

import com.sparta.soundsea.security.jwt.JwtAuthFilter;
import com.sparta.soundsea.security.jwt.JwtUtil;
import com.sparta.soundsea.security.jwt.exception.JwtExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true) // Filter 확인을 위해 Debug 설정, 배포 시 옵션 변경
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    @Bean // 비밀번호 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().
                requestMatchers(PathRequest.toH2Console()); // H2 콘솔 관련된 Path 예외 설정
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF(Cross-site request forgery) 비활성화 설정
        http.csrf().disable();

        // 2. Session 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 3. Request에 대한 인증/인가
        http.authorizeRequests().
                // 3-1. Authentication 예외 처리
                // 3-1-1. SignUp, Login API 인증 예외 처리
                antMatchers("/api/signup").permitAll().
                antMatchers("/api/login").permitAll().
                // 3-1-2. music 조회 관련 API 예외 처리
                antMatchers("/api/music").permitAll().
                antMatchers("/api/music/{path:[0-9]*}").permitAll().
                anyRequest().authenticated();

        // 4. Filter 등록
        // 4-1. JWT Filter 등록
        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthFilter.class);
        // 4-2. OAuth Filter 등록

        return http.build();
    }
}
