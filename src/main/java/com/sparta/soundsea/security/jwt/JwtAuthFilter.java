package com.sparta.soundsea.security.jwt;


import com.sparta.soundsea.security.jwt.exception.CustomSecurityException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sparta.soundsea.common.exception.ExceptionMessage.INVALID_TOKEN_MSG;

@Slf4j  // Console에 Log 찍기 위해 선언
@RequiredArgsConstructor
// Request 한 번에 Filter 한 번 실행을 보장하는 OncePerRequestFilter extend
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, CustomSecurityException {
        // 1. Filter가 적용되고 있는 uri 추출
        String uri = request.getRequestURI();

        // 2. Login, SignUp API의 경우 해당 Filter 건너뜀.
        if (uri.contains("/api/login") || uri.contains("/api/signup")){
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Request에서 토큰 추출
        String token = jwtUtil.resolveToken(request, "AccessToken");
        // 1-1. Token에 어떠한 정보도 없는 경우
        // Token not found Exception은 컨트롤러로 위임
        if (token == null){
            setAuthentication("Foreign");
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Token 유효성 검사 및 인증
        // 2-1. Token 유효성 확인
        if(!jwtUtil.validateAccessToken(token, request, response)){
            throw new CustomSecurityException(INVALID_TOKEN_MSG);
        }
        // 3. 사용자 인증
        Claims info = jwtUtil.getUserInfoFromHttpServletRequest(request);
        setAuthentication(info.getSubject());

        // 4. 다음 필터로 보냄
        filterChain.doFilter(request,response);
    }
    // 인증/인가 설정
    private void setAuthentication(String memberName) {
        // 1. Security Context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 2. Authentication 생성
        Authentication authentication = jwtUtil.createAuthentication(memberName);
        // 3. Context에 Authentication 넣기
        context.setAuthentication(authentication);
        // 4. Security Context Holder에 Context 넣기
        SecurityContextHolder.setContext(context);
    }
}
