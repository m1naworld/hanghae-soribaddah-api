package com.sparta.soundsea.music.controller;

import com.sparta.soundsea.common.response.DataResponse;
import com.sparta.soundsea.common.response.Response;
import com.sparta.soundsea.music.dto.RequestCreateMusic;
import com.sparta.soundsea.music.dto.ResponseMusic;
import com.sparta.soundsea.music.service.MusicService;
import com.sparta.soundsea.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.soundsea.common.exception.ExceptionMessage.TOKEN_NOT_FOUND_MSG;
import static com.sparta.soundsea.common.response.ResponseMessage.*;

@Slf4j
@RequestMapping("/api/music")
@RequiredArgsConstructor
@RestController
public class MusicController {

    private final MusicService musicService;


    @PostMapping
    public Response createMusic(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestCreateMusic requestDto) {


        String loginId = userDetails.getUser().getLoginId();
        Long userId = userDetails.getUser().getId();

        if(loginId.equals("Foreign")){
            throw new IllegalArgumentException(TOKEN_NOT_FOUND_MSG.getMsg());
        }

        musicService.create(userId, requestDto);

        return new Response(CREATE_MUSIC_SUCCESS_MSG);
    }


    @GetMapping("")
    public DataResponse findAllMusic(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Boolean checkLogin = false;

        String loginId = userDetails.getUser().getLoginId();

        if(!(loginId.equals("Foreign"))){
            checkLogin = true;
        }

        List<ResponseMusic> listResponseMusic = musicService.findAllMusic();

        return new DataResponse(READ_MUSIC_ALL_SUCCESS_MSG, listResponseMusic, checkLogin);
    }


    @GetMapping("/{id}")
    public DataResponse findOneMusic(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {

        Boolean checkLogin = false;

        Long userId = userDetails.getUser().getId();

        String loginId = userDetails.getUser().getLoginId();

        if(!loginId.equals("Foreign")){
            checkLogin = true;
        }

        ResponseMusic responseMusic = musicService.findOneMusic(id, userId);

        return new DataResponse(READ_MUSIC_ONE_SUCCESS_MSG, responseMusic, checkLogin);
    }


    @PatchMapping("/{id}")
    public Response updateMusic(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestCreateMusic requestDto) {


        String loginId = userDetails.getUser().getLoginId();
        Long userId = userDetails.getUser().getId();

        if(loginId.equals("Foreign")){
            throw new IllegalArgumentException(TOKEN_NOT_FOUND_MSG.getMsg());
        }

        musicService.update(id, userId, requestDto);

        return new Response(UPDATE_MUSIC_SUCCESS_MSG);

    }


    @DeleteMapping("/{id}")
    public Response deleteMusic(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {


        String loginId = userDetails.getUser().getLoginId();
        Long userId = userDetails.getUser().getId();

        if(loginId.equals("Foreign")){
            throw new IllegalArgumentException(TOKEN_NOT_FOUND_MSG.getMsg());
        }

        musicService.delete(id, userId);


        return new Response(DELETE_MUSIC_SUCCESS_MSG);
    }
}



