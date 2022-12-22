package com.sparta.soundsea.comment.controller;

import com.sparta.soundsea.comment.dto.CommentRequestDto;
import com.sparta.soundsea.comment.dto.CommentResponseDto;
import com.sparta.soundsea.comment.service.CommentService;
import com.sparta.soundsea.common.response.Response;
import com.sparta.soundsea.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.soundsea.common.exception.ExceptionMessage.TOKEN_NOT_FOUND_MSG;
import static com.sparta.soundsea.common.response.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/music/{musicId}/comments")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping()
    public Response createComment(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long musicId, @RequestBody CommentRequestDto requestDto) {
        Long userId = userDetails.getUser().getId();
        String foreign = userDetails.getUser().getLoginId();

        if(foreign.equals("Foreign")){
            throw new IllegalArgumentException(TOKEN_NOT_FOUND_MSG.getMsg());
        }
        commentService.createComment(userId, musicId, requestDto);


        return new Response(COMMENT_CREATE_SUCCESS_MSG);
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public Response updateComment(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long commentId, @PathVariable Long musicId, @RequestBody CommentRequestDto requestDto){
        Long userId = userDetails.getUser().getId();

        String foreign = userDetails.getUser().getLoginId();

        if(foreign.equals("Foreign")){
            throw new IllegalArgumentException(TOKEN_NOT_FOUND_MSG.getMsg());
        }
        commentService.updateComment(userId, commentId, musicId, requestDto);



        return new Response(COMMENT_UPDATE_SUCCESS_MSG);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public Response deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long commentId){
        Long userId = userDetails.getUser().getId();
        String foreign = userDetails.getUser().getLoginId();

        if(foreign.equals("Foreign")){
            throw new IllegalArgumentException(TOKEN_NOT_FOUND_MSG.getMsg());
        }
        commentService.deleteComment(commentId, userId);


        return new Response(COMMENT_DELETE_SUCCESS_MSG);
    }
}
