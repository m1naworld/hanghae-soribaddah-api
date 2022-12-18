package com.sparta.soundsea.comment.service;


import com.sparta.soundsea.comment.dto.CommentRequestDto;
import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.comment.entity.Comment;
import com.sparta.soundsea.comment.repository.CommentRepository;
import com.sparta.soundsea.common.exception.ExceptionResponse;
import com.sparta.soundsea.music.entity.Music;
import com.sparta.soundsea.security.jwt.JwtUtil;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.entity.UserRole;
import com.sparta.soundsea.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;

import static com.sparta.soundsea.common.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;


    //댓글 생성
    @Transactional
    public void createComment(CommentRequestDto requestDto, Long music_id, HttpServletRequest httpServletRequest){

        //JWT 유효성 검사 , 사용자 정보 불러오기
        Claims claims = jwtUtil.getUserInfoFromHttpServletRequest(httpServletRequest);

        //JWT 가져온 정보로 사용자 확인
        User user = userRepository.findByLoginId(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException(INVALID_AUTH_TOKEN.getMsg())
        );

        //Music을 id기준으로 불러와 유효한지 확인
//        Music music = musicRepository.findById(music_id).orElseThrow(
//                () -> new IllegalArgumentException(MUSIC_NOT_FOUND.getMsg())
//        );
//
//        //
//        Comment comment = new Comment(requestDto, music);
//
//        //일치할 시 저장
//        commentRepository.save(comment);

    }

    //댓글 수정
    @Transactional
    public void updateComment(CommentRequestDto requestDto, Long comment_id, HttpServletRequest httpServletRequest){

        //JWT 유효성 검사 , 사용자 정보 불러오기
        Claims claims = jwtUtil.getUserInfoFromHttpServletRequest(httpServletRequest);

        //JWT 가져온 정보로 사용자 확인
        User user = userRepository.findByLoginId(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException(INVALID_AUTH_TOKEN.getMsg())
        );

        //comment id로 comment 찾기
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException(COMMENT_NOT_FOUND.getMsg())
        );

        //작성자와 현재 사용자가 일치하는지 확인, 관리자가 아닌지 확인
        if(!comment.getUser().equals(user.getLoginId()) && user.getRole() == UserRole.USER){
            throw new IllegalArgumentException(UNAUTHORIZED_USER.getMsg());
        }

        //일치할 시 수정
//        comment.update(requestDto);

    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long comment_id, HttpServletRequest httpServletRequest){

        //JWT 유효성 검사 , 사용자 정보 불러오기
        Claims claims = jwtUtil.getUserInfoFromHttpServletRequest(httpServletRequest);

        //JWT 가져온 정보로 사용자 확인
        User user = userRepository.findByLoginId(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException(INVALID_AUTH_TOKEN.getMsg())
        );

        //comment id로 comment 찾기
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException(COMMENT_NOT_FOUND.getMsg())
        );

        //작성자와 현재 사용자가 일치하는지 확인, 관리자가 아닌지 확인
        if(!comment.getUser().equals(user.getLoginId()) && user.getRole() == UserRole.USER){
            throw new IllegalArgumentException(UNAUTHORIZED_USER.getMsg());
        }

        //일치할 시 삭제
        commentRepository.deleteById(comment_id);

    }
}
