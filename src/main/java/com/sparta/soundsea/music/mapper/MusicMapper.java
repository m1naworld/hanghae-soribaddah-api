package com.sparta.soundsea.music.mapper;

import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.music.dto.RequestCreateMusic;
import com.sparta.soundsea.music.dto.ResponseMusic;
import com.sparta.soundsea.music.entity.Music;
import com.sparta.soundsea.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MusicMapper {
    public Music toMusic(User user, RequestCreateMusic requestDto, String storedFileName) {
        return new Music(requestDto.getTitle(), requestDto.getContents(), requestDto.getArtist(), storedFileName, user);
    }

    public ResponseMusic toResponse(Music music) {
        return ResponseMusic.builder()
                .musicId(music.getId())
                .title(music.getTitle())
                .artist(music.getArtist())
                .nickname(music.getUser().getNickname())
                .contents(music.getContents())
                .image(music.getImage())
                .createdAt(music.getCreatedAt())
                .modifiedAt(music.getLastModifiedAt())
//                .commentList(music.getComments())
                .build();
    }

    public ResponseMusic toResponse(Music music, List<CommentResponseDto> commentResponseDtoList, Boolean musicIsMine){
        return ResponseMusic.builder()
                .musicId(music.getId())
                .title(music.getTitle())
                .artist(music.getArtist())
                .nickname(music.getUser().getNickname())
                .contents(music.getContents())
                .image(music.getImage())
                .createdAt(music.getCreatedAt())
                .modifiedAt(music.getLastModifiedAt())
                .commentList(commentResponseDtoList)
                .musicIsMine(musicIsMine)
                .build();
    }

}
