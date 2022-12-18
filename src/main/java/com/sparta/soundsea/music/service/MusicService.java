package com.sparta.soundsea.music.service;

import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.comment.entity.Comment;
import com.sparta.soundsea.comment.repository.CommentRepository;
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

import java.util.ArrayList;
import java.util.List;

import static com.sparta.soundsea.common.exception.ExceptionMessage.USER_NOT_FOUND_ERROR_MSG;

@Service
@Slf4j
@RequiredArgsConstructor
public class MusicService {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final CommentRepository commentRepository;
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

    //음악 전체 조회
    public List<ResponseMusic> findAllMusic() {

        List<ResponseMusic> allResponseMusic = new ArrayList<>();

        List<Music> allMusic = musicRepository.findAllByOrderByModifiedAtDesc();

        for(Music music : allMusic){
             allResponseMusic.add(musicMapper.toResponse(music));
        }

        return allResponseMusic;
    }




    //선택 음악 상세페이지 조회
    @Transactional(readOnly = true)
    public ResponseMusic findOneMusic(Long musicId){

        Music oneMusic = musicRepository.findById(musicId).orElseThrow(
                () -> new IllegalArgumentException("선택한 음악은 존재하지 않습니다.")
        );

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> comments = commentRepository.findAllByMusic_IdOrderByCreatedAtDesc(musicId);
        for(Comment comment : comments){
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        return musicMapper.toResponse(oneMusic, commentResponseDtoList);
    }



}
