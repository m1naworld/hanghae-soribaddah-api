package com.sparta.soundsea.comment.service;


import com.sparta.soundsea.comment.dto.CommentRequestDto;
import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.comment.entity.Comment;
import com.sparta.soundsea.comment.mapper.CommentMapper;
import com.sparta.soundsea.comment.repository.CommentRepository;
import com.sparta.soundsea.music.entity.Music;
import com.sparta.soundsea.music.repository.MusicRepository;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.soundsea.common.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final MusicRepository musicRepository;

    private final CommentMapper commentMapper;


    //댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long userId, Long musicId, CommentRequestDto requestDto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException(USER_NOT_FOUND_ERROR_MSG.getMsg()));

        Music music = musicRepository.findById(musicId).orElseThrow(
                () -> new NullPointerException(MUSIC_NOT_FOUND.getMsg())
        );

       Comment comment = commentMapper.toComment(user, requestDto, music);

       //일치할 시 저장
        commentRepository.save(comment);

        return commentMapper.toResponse(comment);

    }


    //댓글 수정
    @Transactional
    public void updateComment(Long userId,Long commentId, Long musicId, CommentRequestDto requestDto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException(USER_NOT_FOUND_ERROR_MSG.getMsg()));

        //comment 찾아오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException(COMMENT_NOT_FOUND.getMsg())
        );

        Music music = musicRepository.findById(musicId).orElseThrow(
                () -> new NullPointerException(MUSIC_NOT_FOUND.getMsg())
        );

        //작성자와 현재 사용자가 일치하는지 확인, 관리자가 아닌지 확인
        if (!comment.getUser().getLoginId().equals(user.getLoginId())) {
            throw new IllegalArgumentException(UNAUTHORIZED_USER.getMsg());
        }

        //일치할 시 수정
        String updateComment = requestDto.getContents();
        comment.update(updateComment);
//        commentMapper.toResponse(comment);

    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException(USER_NOT_FOUND_ERROR_MSG.getMsg()));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException(COMMENT_NOT_FOUND.getMsg())
        );

        //작성자와 현재 사용자가 일치하는지 확인, 관리자가 아닌지 확인
        if (!comment.getUser().getLoginId().equals(user.getLoginId())) {
            throw new IllegalArgumentException(UNAUTHORIZED_USER.getMsg());
        }

        commentRepository.deleteById(commentId);

    }

}
