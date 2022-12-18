package com.sparta.soundsea.music.dto;

import com.sparta.soundsea.comment.dto.CommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ResponseMusic {

    private Long musicId;
    private String title;
    private String artist;
    private String nickname;
    private String contents;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

}
