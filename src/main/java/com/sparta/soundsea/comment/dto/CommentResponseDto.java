package com.sparta.soundsea.comment.dto;


import com.sparta.soundsea.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long commentId;

    private String loginId;

    private String contents;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Boolean commentIsMine;


    public CommentResponseDto(Comment comment, Boolean commentIsMine) {

        this.commentId = comment.getId();
        this.loginId = comment.getUser().getLoginId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.lastModifiedAt = comment.getLastModifiedAt();
        this.nickname = comment.getUser().getNickname();
        this.commentIsMine = commentIsMine;
    }
}
