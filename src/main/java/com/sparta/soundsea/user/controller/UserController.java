package com.sparta.soundsea.user.controller;

import com.sparta.soundsea.common.response.Response;
import com.sparta.soundsea.user.dto.OAuthLoginDto;
import com.sparta.soundsea.user.dto.RequestLoginUserDto;
import com.sparta.soundsea.user.dto.RequestSignUpUserDto;
import com.sparta.soundsea.user.service.UserKakaoService;
import com.sparta.soundsea.user.service.UserNaverService;
import com.sparta.soundsea.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.sparta.soundsea.common.response.ResponseMessage.LOGIN_USER_SUCCESS_MSG;
import static com.sparta.soundsea.common.response.ResponseMessage.SIGNUP_USER_SUCCESS_MSG;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final NaverOauth naverOauth;
    private final UserNaverService userNaverService;
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
    public String naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response){
        userNaverService.naverLogin(naverOauth.getLoginDtoFromNaver(code, state), response);
        return "redirect:/api/music";
    }

    @GetMapping("/login/kakao")
    public Response kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        String kakaoAccessToken = userKakaoService.getKakaoAccessToken(code);
        OAuthLoginDto kakao= userKakaoService.createKakaoUser(kakaoAccessToken);
        userService.OAuthLogin(kakao, response);
        return new Response(LOGIN_USER_SUCCESS_MSG);
    }
}
