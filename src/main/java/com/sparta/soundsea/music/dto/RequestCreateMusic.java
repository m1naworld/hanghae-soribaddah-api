package com.sparta.soundsea.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestCreateMusic {

    private String title;
    private String artist;
    private String image;
    private String contents;

}
