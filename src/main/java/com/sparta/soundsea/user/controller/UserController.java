package com.sparta.soundsea.user.controller;

import com.sparta.soundsea.common.response.Response;
import com.sparta.soundsea.user.dto.RequestSignUpUserDto;
import com.sparta.soundsea.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.soundsea.common.response.ResponseMessage.LOGIN_USER_SUCCESS_MSG;
import static com.sparta.soundsea.common.response.ResponseMessage.SIGNUP_USER_SUCCESS_MSG;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public Response signUp(@RequestBody RequestSignUpUserDto requestSignUpUserDto) {
        userService.signUp(requestSignUpUserDto.toSignUpUserDto());
        return new Response(SIGNUP_USER_SUCCESS_MSG);
    }

}
