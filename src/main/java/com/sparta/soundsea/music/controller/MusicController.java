package com.sparta.soundsea.music.controller;

import com.sparta.soundsea.common.response.Response;
import com.sparta.soundsea.music.dto.RequestCreateMusic;
import com.sparta.soundsea.music.dto.ResponseMusic;
import com.sparta.soundsea.music.service.MusicService;
import com.sparta.soundsea.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.soundsea.common.response.ResponseMessage.CREATE_MUSIC_SUCCESS_MSG;

@Slf4j
@RequestMapping("/api/music")
@RequiredArgsConstructor
@RestController
public class MusicController {

    private final MusicService musicService;

    //추천음악 등록
    @PostMapping("/new")
    public Response createMusic(@AuthenticationPrincipal CustomUserDetails userDeatils, @RequestBody RequestCreateMusic requestDto){
        //UserDetails에서 userId 뽑아오기
        Long userId = userDeatils.getUser().getId();
        ResponseMusic result = musicService.create(userId, requestDto);

        //확인용
        log.info("등록 결과 ResponseMusic = " + result);

        return new Response(CREATE_MUSIC_SUCCESS_MSG);
    }
}

