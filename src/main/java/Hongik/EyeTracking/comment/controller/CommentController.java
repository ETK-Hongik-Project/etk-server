package Hongik.EyeTracking.comment.controller;

import Hongik.EyeTracking.auth.interfaces.CurrentUserUsername;
import Hongik.EyeTracking.comment.dto.request.CreateCommentRequestDto;
import Hongik.EyeTracking.comment.dto.response.CreateCommentResponseDto;
import Hongik.EyeTracking.comment.dto.response.ReadCommentResponseDto;
import Hongik.EyeTracking.comment.dto.response.ReadUserCommentResponseDto;
import Hongik.EyeTracking.comment.service.CommentService;
import Hongik.EyeTracking.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "post에 comment 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "comment 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, postId를 가지는 post가 존재하지 않는 경우")
    })
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<BaseResponse<CreateCommentResponseDto>> createComment(@CurrentUserUsername String username, @PathVariable("postId") Long postId, @Valid @RequestBody CreateCommentRequestDto requestDto) {
        CreateCommentResponseDto response = commentService.createComment(username, postId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "comment에 reply 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "reply 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, commentId를 가지는 comment가 존재하지 않는 경우")
    })
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<CreateCommentResponseDto>> createReply(@CurrentUserUsername String username, @PathVariable("commentId") Long commentId, @Valid @RequestBody CreateCommentRequestDto requestDto) {
        CreateCommentResponseDto response = commentService.createReply(username, commentId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "post의 모든 comment 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "comment 조회 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "postId를 가지는 post가 존재하지 않는 경우")
    })
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<BaseResponse<List<ReadCommentResponseDto>>> readComment(@PathVariable("postId") Long postId) {
        List<ReadCommentResponseDto> responses = commentService.readCommentsOfPost(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(responses));
    }

    @Operation(summary = "로그인 한 유저의 모든 comment 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "comment 조회 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "postId를 가지는 post가 존재하지 않는 경우")
    })
    @GetMapping("/comments")
    public ResponseEntity<BaseResponse<List<ReadUserCommentResponseDto>>> readUserComments(@CurrentUserUsername String username) {
        List<ReadUserCommentResponseDto> responses = commentService.readUserComments(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(responses));
    }
    
    @Operation(summary = "로그인 한 유저의 comment 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "comment 제거"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, commentId를 가지는 comment가 존재하지 않는 경우"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "유저의 comment가 아닌 경우")
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse> deleteComment(@CurrentUserUsername String username, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(username, commentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
