package com.sparta.soundsea.music.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
//    private List<ResponseComment> commentList;

}
