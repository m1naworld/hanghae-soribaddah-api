package com.sparta.soundsea.comment.dto;


import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private Long commentId;

    private String contents;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;


}
