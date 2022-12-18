package com.sparta.soundsea.comment.dto;

import com.sparta.soundsea.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;

    private String loginId;

    private String contents;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    public CommentResponseDto(Comment comment){

        this.commentId = comment.getId();
        this.loginId = comment.getUser().getLoginId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.lastModifiedAt = comment.getLastModifiedAt();



    }
}
