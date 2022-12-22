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

import static com.sparta.soundsea.common.exception.ExceptionMessage.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class MusicService {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final CommentRepository commentRepository;
    private final MusicMapper musicMapper;


    @Transactional
    public ResponseMusic create(Long userId, RequestCreateMusic requestDto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(USER_NOT_FOUND_ERROR_MSG.getMsg()));
        Music newMusic = musicMapper.toMusic(user, requestDto);

        musicRepository.save(newMusic);

        return musicMapper.toResponse(newMusic);

    }

    @Transactional(readOnly = true)
    public List<ResponseMusic> findAllMusic() {

        List<ResponseMusic> allResponseMusic = new ArrayList<>();

        List<Music> allMusic = musicRepository.findAllByOrderByLastModifiedAtDesc();

        for (Music music : allMusic) {
            allResponseMusic.add(musicMapper.toResponse(music));
        }

        return allResponseMusic;
    }



    @Transactional(readOnly = true)
    public ResponseMusic findOneMusic(Long musicId, Long userId) {

        Boolean musicIsMine = false;
        Boolean commentIsMine;

        Music oneMusic = musicRepository.findById(musicId).orElseThrow(
                () -> new IllegalArgumentException(MUSIC_NOT_FOUND.getMsg())
        );

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> comments = commentRepository.findAllByMusic_IdOrderByCreatedAtDesc(musicId);
        for (Comment comment : comments) {

            if(comment.getUser().getId().equals(userId)){
                commentIsMine = true;
            } else {
                commentIsMine = false;
            }

            commentResponseDtoList.add(new CommentResponseDto(comment, commentIsMine));
        }

        if(oneMusic.getUser().getId().equals(userId)){
            musicIsMine = true;
        }

        return musicMapper.toResponse(oneMusic, commentResponseDtoList, musicIsMine);
    }


    @Transactional
    public void update(Long musicId, Long userId, RequestCreateMusic requestDto) {

        Music oneMusic = musicRepository.findById(musicId).orElseThrow(
                () -> new IllegalArgumentException(MUSIC_NOT_FOUND.getMsg())
        );

        if (oneMusic.getUser().getId().equals(userId)) {
            oneMusic.updateMusic(requestDto);
        } else {
            throw new IllegalArgumentException(INVALID_AUTH_TOKEN.getMsg());
        }

    }


    @Transactional
    public void delete(Long musicId, Long userId) {

        Music oneMusic = musicRepository.findById(musicId).orElseThrow(
                () -> new NullPointerException(MUSIC_NOT_FOUND.getMsg())
        );

        if (oneMusic.getUser().getId().equals(userId)) {
            musicRepository.delete(oneMusic);
        } else {
            throw new IllegalArgumentException(INVALID_AUTH_TOKEN.getMsg());
        }

    }


}