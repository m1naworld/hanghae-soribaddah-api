package com.sparta.soundsea.comment.mapper;

import com.sparta.soundsea.comment.dto.CommentRequestDto;
import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.comment.entity.Comment;
import com.sparta.soundsea.music.entity.Music;
import com.sparta.soundsea.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment toComment(User user, CommentRequestDto requestDto, Music music) {
        return new Comment(requestDto.getContents(), user, music);
    }

    public CommentResponseDto toResponse(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .nickname(comment.getUser().getNickname())
                .contents(comment.getContents())
                .createdAt(comment.getCreatedAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .build();
    }
}
