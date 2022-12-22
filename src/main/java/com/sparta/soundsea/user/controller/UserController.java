package com.sparta.soundsea.user.controller;

import com.sparta.soundsea.common.response.Response;
import com.sparta.soundsea.user.dto.OAuthLoginDto;
import com.sparta.soundsea.user.dto.RequestLoginUserDto;
import com.sparta.soundsea.user.dto.RequestSignUpUserDto;
import com.sparta.soundsea.user.service.UserKakaoService;
import com.sparta.soundsea.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.net.URI;

import static com.sparta.soundsea.common.response.ResponseMessage.LOGIN_USER_SUCCESS_MSG;
import static com.sparta.soundsea.common.response.ResponseMessage.SIGNUP_USER_SUCCESS_MSG;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final NaverOauth naverOauth;
    private final UserKakaoService userKakaoService;

    @PostMapping("/signup")
    public Response signUp(@RequestBody RequestSignUpUserDto requestSignUpUserDto) {
        userService.signUp(requestSignUpUserDto.toSignUpUserDto());
        return new Response(SIGNUP_USER_SUCCESS_MSG);
    }

    @PostMapping("/login")
    public Response login(@RequestBody RequestLoginUserDto requestLoginUserDto, HttpServletResponse response) {
        userService.login(requestLoginUserDto.toLoginUserDto(), response);
        return new Response(LOGIN_USER_SUCCESS_MSG);
    }

    @GetMapping("/login/naver/callback")
    public ResponseEntity<?> naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response){
        userService.OAuthLogin(naverOauth.getLoginDtoFromNaver(code, state), response);

        // Header를 통해 redirect 경로 설정, RestController를 사용하기 때문에 일반적인 방법으로 redirect 안됨.
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://soribaddah.s3-website.ap-northeast-2.amazonaws.com/login"));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/login/kakao")
    public Response kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        String kakaoAccessToken = userKakaoService.getKakaoAccessToken(code);
        OAuthLoginDto kakao= userKakaoService.createKakaoUser(kakaoAccessToken);
        userService.OAuthLogin(kakao, response);
        return new Response(LOGIN_USER_SUCCESS_MSG);
    }
}
