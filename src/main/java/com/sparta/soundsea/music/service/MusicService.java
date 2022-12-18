package com.sparta.soundsea.music.service;

import com.sparta.soundsea.music.dto.RequestCreateMusic;
import com.sparta.soundsea.music.dto.ResponseMusic;
import com.sparta.soundsea.music.entity.Music;
import com.sparta.soundsea.music.mapper.MusicMapper;
import com.sparta.soundsea.music.repository.MusicRepository;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.soundsea.common.exception.ExceptionMessage.USER_NOT_FOUND_ERROR_MSG;

@Service
@Slf4j
@RequiredArgsConstructor
public class MusicService {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final MusicMapper musicMapper;


    //추천음악 등록

    @Transactional
    public ResponseMusic create(Long userId, RequestCreateMusic requestDto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(USER_NOT_FOUND_ERROR_MSG.getMsg()));
        Music newMusic = musicMapper.toMusic(user, requestDto);

        //확인
        log.info("user = " + user);
        log.info("newMusic = " + newMusic);

        musicRepository.save(newMusic);

        return musicMapper.toResponse(newMusic);

    }
}
