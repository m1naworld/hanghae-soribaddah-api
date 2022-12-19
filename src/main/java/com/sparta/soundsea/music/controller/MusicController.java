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

import static com.sparta.soundsea.common.response.ResponseMessage.*;

@Slf4j
@RequestMapping("/api/music")
@RequiredArgsConstructor
@RestController
public class MusicController {

    private final MusicService musicService;

    //추천음악 등록
    @PostMapping
    public Response createMusic(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestCreateMusic requestDto) {
        //UserDetails에서 userId 뽑아오기
        Long userId = userDetails.getUser().getId();
        ResponseMusic result = musicService.create(userId, requestDto);

        //확인용
        log.info("등록 결과 ResponseMusic = " + result);

        return new Response(CREATE_MUSIC_SUCCESS_MSG);
    }


    //음악 전체 조회
    @GetMapping("")
    public DataResponse findAllMusic() {
        List<ResponseMusic> listResponseMusic = musicService.findAllMusic();

        return new DataResponse(READ_MUSIC_ALL_SUCCESS_MSG, listResponseMusic);
    }


    //선택 음악 상세페이지 조회
    @GetMapping("/{id}")
    public DataResponse findOneMusic(@PathVariable Long id) {
        ResponseMusic responseMusic = musicService.findOneMusic(id);

        return new DataResponse(READ_MUSIC_ONE_SUCCESS_MSG, responseMusic);
    }


    //추천 음악 수정
    @PatchMapping("/{id}")
    public Response updateMusic(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestCreateMusic requestDto) {
        //UserDetails에서 userId 뽑아오기
        Long userId = userDetails.getUser().getId();
        musicService.update(id, userId, requestDto);

        //확인용
//        log.info("수정 결과 ResponseMusic = " + result);

        return new Response(UPDATE_MUSIC_SUCCESS_MSG);

    }


    //추천 음악 삭제
    @DeleteMapping("/{id}")
    public Response deleteMusic(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        //UserDetails에서 userId 뽑아오기
        Long userId = userDetails.getUser().getId();
        musicService.delete(id, userId);

        return new Response(DELETE_MUSIC_SUCCESS_MSG);
    }
}



