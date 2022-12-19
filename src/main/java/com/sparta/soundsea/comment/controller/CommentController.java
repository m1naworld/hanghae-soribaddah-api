package com.sparta.soundsea.comment.controller;

import com.sparta.soundsea.comment.dto.CommentRequestDto;
import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.comment.service.CommentService;
import com.sparta.soundsea.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.sparta.soundsea.common.response.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/new/{music_id}")
    public Response createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long music_id, HttpServletRequest httpServletRequest) {
        commentService.createComment(requestDto, music_id, httpServletRequest);
        return new Response(COMMENT_CREATE_SUCCESS_MSG);
    }

    //댓글 수정
    @PutMapping("/{music_id}/{comment_id}")
    public Response updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long comment_id, HttpServletRequest httpServletRequest){
        commentService.updateComment(requestDto, comment_id, httpServletRequest);
        return new Response(COMMENT_UPDATE_SUCCESS_MSG);
    }

    //댓글 삭제
    @DeleteMapping("/{music_id}/{comment_id}")
    public Response deleteComment(@PathVariable Long comment_id, HttpServletRequest httpServletRequest){
        commentService.deleteComment(comment_id, httpServletRequest);
        return new Response(COMMENT_DELETE_SUCCESS_MSG);
    }
}
