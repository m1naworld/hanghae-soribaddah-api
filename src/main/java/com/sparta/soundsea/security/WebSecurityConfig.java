package com.sparta.soundsea.security;

import com.sparta.soundsea.security.jwt.JwtAuthFilter;
import com.sparta.soundsea.security.jwt.JwtUtil;
import com.sparta.soundsea.security.jwt.exception.JwtExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true) // Filter 확인을 위해 Debug 설정, 배포 시 옵션 변경
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    @Bean // 비밀번호 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().
//                requestMatchers(PathRequest.toH2Console()); // H2 콘솔 관련된 Path 예외 설정
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF(Cross-site request forgery) 비활성화 설정 및 cors 설정
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        // 2. Session 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 3. Request에 대한 인증/인가
        http.authorizeRequests().
                // 3-1. Authentication 예외 처리
                // 3-1-1. SignUp, Login API 인증 예외 처리
                antMatchers("/api/signup").permitAll().
                antMatchers("/api/login").permitAll().
                // 3-1-2. music 조회 관련 API 예외 처리
                antMatchers(HttpMethod.GET, "/api/music*").permitAll().
                anyRequest().authenticated();
        
        // 4. Filter 등록
        // 4-1. JWT Filter 등록
        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthFilter.class);
        // 4-2. OAuth Filter 등록

        return http.build();
    }



    // cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://soribaddah.s3-website.ap-northeast-2.amazonaws.com");
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE")); // 허용할 Http Method
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 내 서버가 응답할 때 json을 js에서 처리할 수 있게 설정
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
